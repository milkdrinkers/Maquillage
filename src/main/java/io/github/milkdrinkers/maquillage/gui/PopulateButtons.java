package io.github.milkdrinkers.maquillage.gui;

import io.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.maquillage.gui.GuiHandler.MaquillageGuiType;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

public class PopulateButtons {

    public static void populateButtons(MaquillageGuiType type, PaginatedGui gui, Player p) {
        headButton(type, gui, p);
        arrowButtons(gui);
    }

    private static void headButton(MaquillageGuiType type, PaginatedGui gui, Player p) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.lore(Collections.singletonList(ColorParser.of(Translation.of("gui.clear"))
            .parseMinimessagePlaceholder("cosmetic", type.toString().toLowerCase()).build()));

        // Sets head name to match selected tag and color
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);
        if (playerData != null) {
            String tag = playerData.getTag().isPresent() ? playerData.getTag().get().getTag() + " " : "";
            String color = playerData.getNameColor().isPresent() ? playerData.getNameColor().get().getColor() : "<white>";

            skullMeta.displayName(ColorParser.of(tag + color + p.getName()).build().decoration(TextDecoration.ITALIC, false));
        }

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        skull.setItemMeta(skullMeta);

        gui.setItem(6, 5, ItemBuilder.from(skull).asGuiItem(event -> {
            switch (type) {
                case COLOR -> {
                    NameColorHolder.clearPlayerColor(p);
                    GuiHandler.reloadGui(type, gui, p);
                }
                case TAG -> {
                    TagHolder.clearPlayerTag(p);
                    GuiHandler.reloadGui(type, gui, p);
                }
            }
        }));
    }

    private static void arrowButtons(PaginatedGui gui) {
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.displayName(ColorParser.of(Translation.of("gui.next-page")).build().decoration(TextDecoration.ITALIC, false));
        nextPage.setItemMeta(nextPageMeta);
        gui.setItem(6, 6, ItemBuilder.from(nextPage).asGuiItem(event -> {
            gui.next();
        }));


        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.displayName(ColorParser.of(Translation.of("gui.previous-page")).build().decoration(TextDecoration.ITALIC, false));
        prevPage.setItemMeta(prevPageMeta);
        gui.setItem(6, 4, ItemBuilder.from(prevPage).asGuiItem(event -> {
            gui.previous();
        }));
    }
}
