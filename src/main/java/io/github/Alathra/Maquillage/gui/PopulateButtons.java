package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import io.github.Alathra.Maquillage.gui.GuiHandler.MaquillageGuiType;

import java.util.Collections;

public class PopulateButtons {

    public static void populateButtons (MaquillageGuiType type, PaginatedGui gui, Player p) {
        headButton(type, gui, p);
        if (gui.getPagesNum() > 1)
            arrowButtons(gui);
    }

    private static void headButton (MaquillageGuiType type, PaginatedGui gui, Player p) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.lore(Collections.singletonList(ColorParser.of("<red>Click to clear your " + type.toString().toLowerCase()).build()));

        // Sets head name to match selected tag and color
        String tag = TagHandler.doesPlayerHaveTag(p) ? TagHandler.getPlayerTagString(p) + " " : "";
        String color = NameColorHandler.doesPlayerHaveColor(p) ? NameColorHandler.getPlayerColorString(p) : "<white>";

        skullMeta.displayName(ColorParser.of(tag + color + p.getName()).build());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()));
        skull.setItemMeta(skullMeta);

        gui.setItem(6, 5, ItemBuilder.from(skull).asGuiItem(event -> {
            switch (type) {
                case COLOR -> {
                    NameColorHandler.clearPlayerColor(p);
                    UpdateDisplayName.clearPlayerNameColor(p);
                    GuiHandler.reloadGui(type, gui, p);
                }
                case TAG ->  {
                    TagHandler.clearPlayerTag(p);
                    UpdateDisplayName.clearPlayerTag(p);
                    GuiHandler.reloadGui(type, gui, p);
                }
            }
        }));
    }

    private static void arrowButtons (PaginatedGui gui) {
        boolean lastPage = gui.getNextPageNum() == gui.getCurrentPageNum();
        if (!lastPage) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            nextPageMeta.displayName(ColorParser.of("<green>Next page").build());
            nextPage.setItemMeta(nextPageMeta);
            gui.setItem(6, 6, ItemBuilder.from(nextPage).asGuiItem(event -> gui.next()));
        }

        boolean firstPage = gui.getPrevPageNum() == gui.getCurrentPageNum();
        if (!firstPage) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevPageMeta = prevPage.getItemMeta();
            prevPageMeta.displayName(ColorParser.of("<green>Previous page").build());
            prevPage.setItemMeta(prevPageMeta);
            gui.setItem(6, 4, ItemBuilder.from(prevPage).asGuiItem(event -> gui.previous()));
        }
    }
}
