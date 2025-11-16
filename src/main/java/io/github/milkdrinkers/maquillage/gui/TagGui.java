package io.github.milkdrinkers.maquillage.gui;

import dev.triumphteam.gui.builder.item.PaperItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.gui.edit.TagEditor;
import io.github.milkdrinkers.maquillage.module.cosmetic.BaseCosmetic;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.wordweaver.Translation;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;
import java.util.Optional;

public class TagGui extends AbstractGui {
    @Override
    public void open(Player p, boolean editorMode) {
        super.open(p, editorMode);
        final PaginatedGui gui = dev.triumphteam.gui.guis.Gui.paginated()
            .title(editorMode ? translate("gui.tag.title-editor", p) : translate("gui.tag.title", p))
            .rows(6)
            .disableItemPlace()
            .disableItemSwap()
            .disableItemDrop()
            .disableItemTake()
            .disableOtherActions()
            .create();

        gui.setCloseGuiAction(e -> onClose(gui, e));

        populate(gui, p);

        gui.open(p);
    }

    @Override
    void reload(PaginatedGui gui, Player p) {
        gui.clearPageItems(false);
        populate(gui, p);
        gui.update();
    }

    @Override
    public void populateDecoration(PaginatedGui gui, Player p) {
        final GuiItem greyGlassPane = PaperItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
            .name(Component.empty())
            .asGuiItem();

        gui.getFiller().fillBorder(greyGlassPane);
    }

    @Override
    void populateButtons(PaginatedGui gui, Player p) {
        new PopulateButtons(this).populate(gui, p);
    }

    @Override
    void populateContent(PaginatedGui gui, Player p) {
        new PopulateContent(this).populate(gui, p);
    }

    private static final class PopulateButtons {
        private static final ItemStack nextButton = new ItemStack(Material.ARROW);
        private static final ItemStack prevButton = new ItemStack(Material.ARROW);
        private static final ItemStack infoButton = new ItemStack(Material.PLAYER_HEAD);
        private static final ItemStack createButton = new ItemStack(Material.CRAFTING_TABLE);

        private static final Sound SOUND_CLICK = Sound.sound(Key.key("ui.button.click"), Sound.Source.AMBIENT, 1.0f, 1.0f);
        private static final Sound SOUND_CLICK_FAIL = Sound.sound(Key.key("minecraft:block.end_portal_frame.fill"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private final TagGui parentGui;

        private PopulateButtons(TagGui parentGui) {
            this.parentGui = parentGui;
        }

        /**
         * Add all buttons to the gui
         *
         * @param gui    gui
         * @param p      player
         */
        public void populate(PaginatedGui gui, Player p) {
            nextButton(gui, p);
            previousButton(gui, p);
            infoButton(gui, p);
            createButton(gui, p);
        }

        /**
         * Add button to gui
         *
         * @param gui gui
         * @param p   player
         */
        private void nextButton(PaginatedGui gui, Player p) {
            final ItemStack nextPage = nextButton.clone();
            nextPage.editMeta(meta -> meta.customName(translate("gui.next-page", p)));
            gui.setItem(6, 6, PaperItemBuilder.from(nextPage).asGuiItem(event -> {
                if (gui.next())
                    p.playSound(SOUND_CLICK);
                else
                    p.playSound(SOUND_CLICK_FAIL);
            }));
        }

        /**
         * Add button to gui
         *
         * @param gui gui
         * @param p   player
         */
        private void previousButton(PaginatedGui gui, Player p) {
            final ItemStack prevPage = prevButton.clone();
            prevPage.editMeta(meta -> meta.customName(translate("gui.previous-page", p)));
            gui.setItem(6, 4, PaperItemBuilder.from(prevPage).asGuiItem(event -> {
                if (gui.previous())
                    p.playSound(SOUND_CLICK);
                else
                    p.playSound(SOUND_CLICK_FAIL);
            }));
        }

        /**
         * Add button to gui
         *
         * @param gui gui
         * @param p   player
         */
        private void infoButton(PaginatedGui gui, Player p) {
            final ItemStack item = infoButton.clone();

            item.editMeta(SkullMeta.class, meta -> {
                final PlayerData data = PlayerDataHolder.getInstance().getPlayerData(p);
                Objects.requireNonNull(data, "Player data was null when rendering GUI!");

                final String namecolor = data.getNameColor().map(NameColor::getColor).orElse("<white>") + p.getName();
                final String tag = data.getTag().map(tag1 -> tag1.getTag() + "<reset> ").orElse("");

                meta.customName(
                    ColorParser.of(Translation.of("gui.tag.clear-item.name"))
                        .papi(p)
                        .mini(p)
                        .with("preview_namecolor", namecolor)
                        .with("preview_tag", tag)
                        .build()
                );
                meta.lore(
                    Translation.ofList("gui.tag.clear-item.lore").stream()
                        .map(s -> ColorParser.of(s)
                            .papi(p)
                            .mini(p)
                            .with("preview_namecolor", namecolor)
                            .with("preview_tag", tag)
                            .build()
                        )
                        .toList()
                );
                meta.setOwningPlayer(p);
            });

            gui.setItem(6, 5, PaperItemBuilder.from(item).asGuiItem(e -> {
                TagHolder.clearPlayerTag(p);
                p.playSound(SOUND_CLICK);
                gui.clearPageItems(false);
                parentGui.populateContent(gui, p);
                populate(gui, p);
                gui.update();
            }));
        }

        /**
         * Add button to gui
         *
         * @param gui gui
         * @param p   player
         */
        private void createButton(PaginatedGui gui, Player p) {
            if (!parentGui.isEditorMode())
                return;

            final ItemStack createItem = createButton.clone();
            createItem.editMeta(meta -> meta.customName(translate("gui.previous-page", p)));
            gui.setItem(1, 5, PaperItemBuilder.from(createItem).asGuiItem(event -> {
                p.playSound(SOUND_CLICK);
                TagEditor.create(
                    p,
                    (response, player, data) -> {
                        if (!player.hasPermission("maquillage.command.admin.create.tag")) {
                            player.sendMessage(Bukkit.permissionMessage());
                            return;
                        }

                        final boolean success = TagHolder.getInstance().add(data.tag(), data.permission(), data.label(), (int) Math.floor(data.weight())) != -1;
                        if (success) {
                            p.playSound(SOUND_CLICK);
                            player.sendMessage(Translation.as("commands.module.tag.create.success"));
                            new TagGui().open(player, parentGui.isEditorMode());
                        } else {
                            p.playSound(SOUND_CLICK_FAIL);
                            player.sendMessage(Translation.as("commands.module.tag.create.failure"));
                        }
                    },
                    (response, player, data) -> {},
                    (response, player, data) -> {
                        new TagGui().open(player, parentGui.isEditorMode());
                    }
                );
            }));
        }
    }

    private static final class PopulateContent {
        private static final ItemStack inactiveButton = new ItemStack(Material.NAME_TAG);
        private static final ItemStack activeButton = new ItemStack(Material.ENCHANTED_BOOK);

        private static final Sound SOUND_SUCCESS = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private static final Sound SOUND_FAIL = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private final TagGui parentGui;

        private PopulateContent(TagGui parentGui) {
            this.parentGui = parentGui;
        }

        /**
         * Add all content to the gui
         *
         * @param gui    gui
         * @param p      player
         */
        public void populate(PaginatedGui gui, Player p) {
            final PlayerData data = PlayerDataHolder.getInstance().getPlayerData(p);
            if (data == null)
                return;

            final Optional<Tag> currentTag = data.getTag();

            TagHolder.getInstance().cacheGet()
                .values()
                .stream()
                .filter(tag -> tag.hasPerm(p))
                .sorted(
                    sorter(
                        Tag::getWeight,
                        Tag::getLabel,
                        SortOrder.DESCENDING,
                        SortOrder.ASCENDING
                    )
                )
                .forEachOrdered(tag -> {
                    final boolean isActive = tag.getDatabaseId() == currentTag.map(BaseCosmetic::getDatabaseId).orElse(-100);

                    final ItemStack item = isActive ? activeButton.clone() : inactiveButton.clone();
                    item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.editMeta(meta -> {
                        final String namecolor = data.getNameColor().map(NameColor::getColor).orElse("<white>") + p.getName();

                        meta.customName(
                            ColorParser.of(Translation.of("gui.tag.item.name"))
                                .papi(p)
                                .mini(p)
                                .with("label", tag.getLabel())
                                .with("preview_namecolor", namecolor)
                                .with("preview_tag", tag.getTag())
                                .build()
                        );
                        meta.lore(
                            Translation.ofList(parentGui.isEditorMode() ? "gui.tag.item.lore-editor" : "gui.tag.item.lore").stream()
                                .map(s -> ColorParser.of(s)
                                    .papi(p)
                                    .mini(p)
                                    .with("label", tag.getLabel())
                                    .with("preview_namecolor", namecolor)
                                    .with("preview_tag", tag.getTag())
                                    .with("active", isActive ? Translation.as("gui.tag.item.active.yes") : Translation.as("gui.tag.item.active.no"))
                                    .build()
                                )
                                .toList()
                        );

                        if (isActive)
                            meta.addEnchant(Enchantment.MENDING, 1, true);
                    });

                    if (!parentGui.isEditorMode()) {
                        if (isActive) {
                            gui.addItem(PaperItemBuilder.from(item).asGuiItem());
                        } else {
                            gui.addItem(PaperItemBuilder.from(item).asGuiItem(e -> onClick(e, gui, p, tag)));
                        }
                    } else {
                        gui.addItem(PaperItemBuilder.from(item).asGuiItem(e -> onClick(e, gui, p, tag)));
                    }
                });
        }

        /**
         * Handle click on content item
         *
         * @param e     event
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private void onClick(InventoryClickEvent e, PaginatedGui gui, Player p, Tag tag) {
            final boolean isLeftClick = e.isLeftClick();
            final boolean isRightClick = e.isRightClick();
            final boolean isShiftClick = e.isShiftClick();
            final boolean isMiddleClick = e.getAction().equals(InventoryAction.CLONE_STACK);

            if (parentGui.isEditorMode()) {
                if (isShiftClick && isRightClick) {
                    onClickDelete(gui, p, tag);
                    return;
                }

                if (isLeftClick || isRightClick) {
                    onClickEdit(gui, p, tag);
                    return;
                }

                if (isMiddleClick) {
                    onClickClone(gui, p, tag);
                    return;
                }
            } else if (!parentGui.isEditorMode() && !isShiftClick && isLeftClick) {
                final boolean success = TagHolder.setPlayerTag(p, tag);

                if (success) {
                    onClickSuccess(gui, p, tag);
                } else {
                    onClickFail(gui, p, tag);
                }
                return;
            }
        }

        /**
         * On click success
         *
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private void onClickSuccess(PaginatedGui gui, Player p, Tag tag) {
            gui.clearPageItems(false);
            populate(gui, p);
            parentGui.populateButtons(gui, p);
            gui.update();
            
            p.playSound(SOUND_SUCCESS);
        }

        /**
         * On click fail
         *
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private void onClickFail(PaginatedGui gui, Player p, Tag tag) {
            gui.close(p, false);
            p.playSound(SOUND_FAIL);
            p.sendMessage(Translation.as("gui.on-cooldown"));
        }

        /**
         * On click edit
         *
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private void onClickEdit(PaginatedGui gui, Player p, Tag tag) {
            if (!p.hasPermission("maquillage.command.admin.edit.tag")) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(Bukkit.permissionMessage());
                return;
            }

            // Opening dialog automatically closes gui
            p.playSound(SOUND_SUCCESS);
            TagEditor.edit(
                p,
                tag,
                (response, player, data) -> {
                    if (!player.hasPermission("maquillage.command.admin.edit.tag")) {
                        player.playSound(SOUND_FAIL);
                        player.sendMessage(Bukkit.permissionMessage());
                        return;
                    }

                    final boolean success = TagHolder.getInstance().update(data.tag(), data.permission(), data.label(), tag.getDatabaseId(), (int) Math.floor(data.weight()));
                    if (success) {
                        player.playSound(SOUND_SUCCESS);
                        new TagGui().open(player, parentGui.isEditorMode());
                    } else {
                        player.playSound(SOUND_FAIL);
                        player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.failure")).build());
                    }
                },
                (response, player, data) -> {
                },
                (response, player, data) -> {
                    new TagGui().open(player, parentGui.isEditorMode());
                }
            );
        }

        /**
         * On click clone
         *
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private void onClickClone(PaginatedGui gui, Player p, Tag tag) {
            if (!p.hasPermission("maquillage.command.admin.create.tag")) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(Bukkit.permissionMessage());
                return;
            }

            // Creates a new tag with the same data as the old one
            final boolean success = TagHolder.getInstance().add(tag.getTag(), tag.getPerm(), tag.getLabel(), tag.getWeight()) != -1;
            if (!success) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(ColorParser.of(Translation.of("commands.module.tag.create.failure")).build());
                return;
            }

            gui.clearPageItems(false);
            populate(gui, p);
            parentGui.populateButtons(gui, p);
            gui.update();

            p.playSound(SOUND_SUCCESS);
        }

        /**
         * On click delete
         *
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private void onClickDelete(PaginatedGui gui, Player p, Tag tag) {
            if (!p.hasPermission("maquillage.command.admin.delete.tag")) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(Bukkit.permissionMessage());
                return;
            }

            // Delete the tag
            final boolean success = TagHolder.getInstance().remove(tag);
            if (!success) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(ColorParser.of(Translation.of("commands.module.tag.delete.failure")).build());
                return;
            }

            gui.clearPageItems(false);
            populate(gui, p);
            parentGui.populateButtons(gui, p);
            gui.update();

            p.playSound(SOUND_SUCCESS);
        }
    }
}
