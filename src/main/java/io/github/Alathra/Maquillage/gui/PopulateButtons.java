package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import io.github.Alathra.Maquillage.gui.GuiHandler.MaquillageGuiType;

import java.util.Collections;

public class PopulateButtons {

    public static void populateButtons (PaginatedGui gui, Player p, MaquillageGuiType type) {
        headButton(gui, p, type);
        if (gui.getPagesNum() > 1)
            arrowButtons(gui);
    }

    private static void headButton (PaginatedGui gui, Player p, MaquillageGuiType type) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        if (type != MaquillageGuiType.ADMIN)
            skullMeta.lore(Collections.singletonList(ColorParser.of("<dark_red>Clicking this will clear your " + type.toString().toLowerCase()).build()));

        // Sets color to white if player does not have a color selected
        if (!NameColorHandler.doesPlayerHaveColor(p))
            skullMeta.displayName(p.displayName().color(NamedTextColor.WHITE));
        // Else, sets skull name to player's displayName
        else
            skullMeta.displayName(p.displayName());

        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        skull.setItemMeta(skullMeta);

        gui.setItem(6, 5, ItemBuilder.from(skull).asGuiItem(event -> {
            switch (type) {
                case COLOR -> {
                    NameColorHandler.clearPlayerColor(p);
                    gui.clearPageItems();
                    PopulateContent.populateColorContent(gui, NameColorHandler.loadedColors, p);
                    populateButtons(gui, p, MaquillageGuiType.COLOR);
                    gui.update();
                }
                case TAG ->  {
                    TagHandler.clearPlayerTag(p);
                    gui.clearPageItems();
                    PopulateContent.populateTagContent(gui, TagHandler.loadedTags, p);
                    populateButtons(gui, p, MaquillageGuiType.TAG);
                    gui.update();
                }
            }
        }));
    }

    private static void arrowButtons (PaginatedGui gui) {
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.displayName(ColorParser.of("<green>Next page").build());
        nextPage.setItemMeta(nextPageMeta);
        gui.setItem(6, 6, ItemBuilder.from(nextPage).asGuiItem(event -> gui.next()));

        ItemStack prevPage = new ItemStack(Material.ARROW);
        ItemMeta prevPageMeta = prevPage.getItemMeta();
        prevPageMeta.displayName(ColorParser.of("<green>Previous page").build());
        prevPage.setItemMeta(prevPageMeta);
        gui.setItem(6, 4, ItemBuilder.from(prevPage).asGuiItem(event -> gui.previous()));
    }

}
