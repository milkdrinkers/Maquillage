package io.github.milkdrinkers.maquillage.gui;

import dev.triumphteam.gui.builder.item.PaperItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.gui.edit.NameColorEditor;
import io.github.milkdrinkers.maquillage.module.cosmetic.BaseCosmetic;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.utility.PermissionUtility;
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

public class NameColorGui extends AbstractGui {
    @Override
    public void open(Player p, boolean editorMode) {
        super.open(p, editorMode);
        final PaginatedGui gui = dev.triumphteam.gui.guis.Gui.paginated()
            .title(editorMode ? translate("gui.namecolor.title-editor", p) : translate("gui.namecolor.title", p))
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
        private static final ItemStack nextButton = new ItemStack(org.bukkit.Material.ARROW);
        private static final ItemStack prevButton = new ItemStack(org.bukkit.Material.ARROW);
        private static final ItemStack infoButton = new ItemStack(org.bukkit.Material.PLAYER_HEAD);
        private static final ItemStack createButton = new ItemStack(Material.CRAFTING_TABLE);

        private static final Sound SOUND_CLICK = Sound.sound(Key.key("ui.button.click"), Sound.Source.AMBIENT, 1.0f, 1.0f);
        private static final Sound SOUND_CLICK_FAIL = Sound.sound(Key.key("minecraft:block.end_portal_frame.fill"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private final NameColorGui parentGui;

        private PopulateButtons(NameColorGui parentGui) {
            this.parentGui = parentGui;
        }

        /**
         * Add all buttons to the gui
         *
         * @param gui          gui
         * @param p            player
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
                    ColorParser.of(Translation.of("gui.namecolor.clear-item.name"))
                        .papi(p)
                        .mini(p)
                        .with("preview_namecolor", namecolor)
                        .with("preview_tag", tag)
                        .build()
                );
                meta.lore(
                    Translation.ofList("gui.namecolor.clear-item.lore").stream()
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
                NameColorHolder.clearPlayerColor(p);
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
                NameColorEditor.create(
                    p,
                    (response, player, data) -> {
                        if (!player.hasPermission("maquillage.command.admin.create.color")) {
                            player.sendMessage(Bukkit.permissionMessage());
                            return;
                        }

                        final boolean success = NameColorHolder.getInstance().add(data.color(), PermissionUtility.sanitizePermission(data.permission()), data.label(), (int) Math.floor(data.weight())) != -1;
                        if (success) {
                            p.playSound(SOUND_CLICK);
                            player.sendMessage(Translation.as("commands.module.namecolor.create.success"));
                            new NameColorGui().open(player, parentGui.isEditorMode());
                        } else {
                            p.playSound(SOUND_CLICK_FAIL);
                            player.sendMessage(Translation.as("commands.module.namecolor.create.failure"));
                        }
                    },
                    (response, player, data) -> {
                    },
                    (response, player, data) -> {
                        new NameColorGui().open(player, parentGui.isEditorMode());
                    }
                );
            }));
        }
    }

    private static final class PopulateContent {
        private static final ItemStack inactiveButton = new ItemStack(Material.GRAY_DYE);
        private static final ItemStack activeButton = new ItemStack(Material.LIME_DYE);

        private static final Sound SOUND_SUCCESS = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private static final Sound SOUND_FAIL = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private final NameColorGui parentGui;

        private PopulateContent(NameColorGui parentGui) {
            this.parentGui = parentGui;
        }

        /**
         * Add all content to the gui
         *
         * @param gui          gui
         * @param p            player
         */
        public void populate(PaginatedGui gui, Player p) {
            final PlayerData data = PlayerDataHolder.getInstance().getPlayerData(p);
            if (data == null)
                return;

            final Optional<NameColor> currentColor = data.getNameColor();

            NameColorHolder.getInstance().cacheGet()
                .values()
                .stream()
                .filter(color -> color.hasPerm(p))
                .sorted(
                    sorter(
                        NameColor::getWeight,
                        NameColor::getLabel,
                        SortOrder.DESCENDING,
                        SortOrder.ASCENDING
                    )
                )
                .forEachOrdered(color -> {
                    final boolean isActive = color.getDatabaseId() == currentColor.map(BaseCosmetic::getDatabaseId).orElse(-100);

                    final ItemStack item = isActive ? activeButton.clone() : inactiveButton.clone();
                    item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.editMeta(meta -> {
                        // Get player tag (we also display this in the preview)
                        final String tag = data.getTag().map(tag1 -> tag1.getTag() + "<reset> ").orElse("");

                        meta.customName(
                            ColorParser.of(Translation.of("gui.namecolor.item.name"))
                                .papi(p)
                                .mini(p)
                                .with("label", color.getColor() + color.getLabel())
                                .with("preview_namecolor", color.getColor() + p.getName())
                                .with("preview_tag", tag)
                                .build()
                        );
                        meta.lore(
                            Translation.ofList(parentGui.isEditorMode() ? "gui.namecolor.item.lore-editor" : "gui.namecolor.item.lore").stream()
                                .map(s -> ColorParser.of(s)
                                    .papi(p)
                                    .mini(p)
                                    .with("label", color.getColor() + color.getLabel())
                                    .with("preview_namecolor", color.getColor() + p.getName())
                                    .with("preview_tag", tag)
                                    .with("active", isActive ? Translation.as("gui.namecolor.item.active.yes") : Translation.as("gui.namecolor.item.active.no"))
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
                            gui.addItem(PaperItemBuilder.from(item).asGuiItem(e -> onClick(e, gui, p, color)));
                        }
                    } else {
                        gui.addItem(PaperItemBuilder.from(item).asGuiItem(e -> onClick(e, gui, p, color)));
                    }
                });
        }

        /**
         * Handle click on content item
         *
         * @param e     event
         * @param gui   gui
         * @param p     player
         * @param color namecolor
         */
        private void onClick(InventoryClickEvent e, PaginatedGui gui, Player p, NameColor color) {
            final boolean isLeftClick = e.isLeftClick();
            final boolean isRightClick = e.isRightClick();
            final boolean isShiftClick = e.isShiftClick();
            final boolean isMiddleClick = e.getAction().equals(InventoryAction.CLONE_STACK);

            if (parentGui.isEditorMode()) {
                if (isShiftClick && isRightClick) {
                    onClickDelete(gui, p, color);
                    return;
                }

                if (isLeftClick || isRightClick) {
                    onClickEdit(gui, p, color);
                    return;
                }

                if (isMiddleClick) {
                    onClickClone(gui, p, color);
                    return;
                }
            } else if (!parentGui.isEditorMode() && !isShiftClick && isLeftClick) {
                final boolean success = NameColorHolder.setPlayerColor(p, color);

                if (success) {
                    onClickSuccess(gui, p, color);
                } else {
                    onClickFail(gui, p, color);
                }
                return;
            }
        }

        /**
         * On click success
         *
         * @param gui   gui
         * @param p     player
         * @param color namecolor
         */
        private void onClickSuccess(PaginatedGui gui, Player p, NameColor color) {
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
         * @param color namecolor
         */
        private void onClickFail(PaginatedGui gui, Player p, NameColor color) {
            gui.close(p, false);
            p.playSound(SOUND_FAIL);
            p.sendMessage(Translation.as("gui.on-cooldown"));
        }

        /**
         * On click edit
         *
         * @param gui   gui
         * @param p     player
         * @param color namecolor
         */
        private void onClickEdit(PaginatedGui gui, Player p, NameColor color) {
            if (!p.hasPermission("maquillage.command.admin.edit.color")) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(Bukkit.permissionMessage());
                return;
            }

            // Opening dialog automatically closes gui
            p.playSound(SOUND_SUCCESS);
            NameColorEditor.edit(
                p,
                color,
                (response, player, data) -> {
                    if (!player.hasPermission("maquillage.command.admin.edit.color")) {
                        player.playSound(SOUND_FAIL);
                        player.sendMessage(Bukkit.permissionMessage());
                        return;
                    }

                    final boolean success = TagHolder.getInstance().update(data.color(), PermissionUtility.sanitizePermission(data.permission()), data.label(), color.getDatabaseId(), (int) Math.floor(data.weight()));
                    if (success) {
                        player.playSound(SOUND_SUCCESS);
                        new NameColorGui().open(player, parentGui.isEditorMode());
                    } else {
                        player.playSound(SOUND_FAIL);
                        player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.failure")).build());
                    }
                },
                (response, player, data) -> {
                },
                (response, player, data) -> {
                    new NameColorGui().open(player, parentGui.isEditorMode());
                }
            );
        }

        /**
         * On click clone
         *
         * @param gui   gui
         * @param p     player
         * @param color namecolor
         */
        private void onClickClone(PaginatedGui gui, Player p, NameColor color) {
            if (!p.hasPermission("maquillage.command.admin.create.color")) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(Bukkit.permissionMessage());
                return;
            }

            // Creates a new namecolor with the same data as the old one
            final boolean success = NameColorHolder.getInstance().add(color.getColor(), color.getPerm(), color.getLabel(), color.getWeight()) != -1;
            if (!success) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.create.failure")).build());
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
         * @param color namecolor
         */
        private void onClickDelete(PaginatedGui gui, Player p, NameColor color) {
            if (!p.hasPermission("maquillage.command.admin.delete.color")) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(Bukkit.permissionMessage());
                return;
            }

            // Delete the namecolor
            final boolean success = NameColorHolder.getInstance().remove(color);
            if (!success) {
                gui.close(p, false);
                p.playSound(SOUND_FAIL);
                p.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.delete.failure")).build());
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
