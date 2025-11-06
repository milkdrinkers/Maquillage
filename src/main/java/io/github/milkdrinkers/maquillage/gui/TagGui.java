package io.github.milkdrinkers.maquillage.gui;

import dev.triumphteam.gui.builder.item.PaperItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
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
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class TagGui extends AbstractGui {
    @Override
    public void open(Player p) {
        final PaginatedGui gui = dev.triumphteam.gui.guis.Gui.paginated()
            .title(translate("gui.tag.title", p))
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
        PopulateButtons.populate(gui, p);
    }

    @Override
    void populateContent(PaginatedGui gui, Player p) {
        PopulateContent.populate(gui, p);
    }

    private static final class PopulateButtons {
        private static final ItemStack nextButton = new ItemStack(Material.ARROW);
        private static final ItemStack prevButton = new ItemStack(Material.ARROW);
        private static final ItemStack infoButton = new ItemStack(Material.PLAYER_HEAD);

        private static final Sound SOUND_CLICK = Sound.sound(Key.key("ui.button.click"), Sound.Source.AMBIENT, 1.0f, 1.0f);
        private static final Sound SOUND_CLICK_FAIL = Sound.sound(Key.key("minecraft:block.end_portal_frame.fill"), Sound.Source.AMBIENT, 0.5f, 1.0F);

        /**
         * Add all buttons to the gui
         *
         * @param gui gui
         * @param p   player
         */
        public static void populate(PaginatedGui gui, Player p) {
            nextButton(gui, p);
            previousButton(gui, p);
            infoButton(gui, p);
        }

        /**
         * Add button to gui
         *
         * @param gui gui
         * @param p   player
         */
        private static void nextButton(PaginatedGui gui, Player p) {
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
        private static void previousButton(PaginatedGui gui, Player p) {
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
        private static void infoButton(PaginatedGui gui, Player p) {
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
                PopulateContent.populate(gui, p);
                PopulateButtons.populate(gui, p);
                gui.update();
            }));
        }
    }

    private final static class PopulateContent {
        private static final ItemStack inactiveButton = new ItemStack(Material.NAME_TAG);
        private static final ItemStack activeButton = new ItemStack(Material.ENCHANTED_BOOK);

        private static final Sound SOUND_SUCCESS = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.AMBIENT, 0.5f, 1.0F);
        private static final Sound SOUND_FAIL = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.AMBIENT, 0.5f, 1.0F);

        /**
         * Add all content to the gui
         *
         * @param gui gui
         * @param p   player
         */
        public static void populate(PaginatedGui gui, Player p) {
            final PlayerData data = PlayerDataHolder.getInstance().getPlayerData(p);
            if (data == null)
                return;

            final Optional<Tag> currentTag = data.getTag();

            TagHolder.getInstance().cacheGet()
                .values()
                .stream()
                .filter(tag -> tag.hasPerm(p))
                .sorted(Comparator.comparing(Tag::getLabel))
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
                            Translation.ofList("gui.tag.item.lore").stream()
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

                    if (isActive) {
                        gui.addItem(PaperItemBuilder.from(item).asGuiItem());
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
        private static void onClick(InventoryClickEvent e, PaginatedGui gui, Player p, Tag tag) {
            final boolean success = TagHolder.setPlayerTag(p, tag);

            if (success) {
                onClickSuccess(gui, p, tag);
            } else {
                onClickFail(gui, p, tag);
            }
        }

        /**
         * On click success
         *
         * @param gui   gui
         * @param p     player
         * @param tag   tag
         */
        private static void onClickSuccess(PaginatedGui gui, Player p, Tag tag) {
            gui.clearPageItems(false);
            PopulateContent.populate(gui, p);
            PopulateButtons.populate(gui, p);
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
        private static void onClickFail(PaginatedGui gui, Player p, Tag tag) {
            gui.close(p, false);
            p.playSound(SOUND_FAIL);
            p.sendMessage(Translation.as("gui.on-cooldown"));
        }
    }
}
