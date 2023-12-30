package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;

public class PopulateContent {

    static ItemStack colorItem = new ItemStack(Material.GRAY_DYE);
    static ItemMeta colorItemMeta = colorItem.getItemMeta();
    static ItemStack selectedColorItem = new ItemStack(Material.LIME_DYE);
    static ItemMeta selectedColorItemMeta = selectedColorItem.getItemMeta();

    public static void populateColorContent(PaginatedGui gui, HashMap<Integer, NameColor> colors, Player p) {
        int selectedColor = NameColorHandler.getPlayerColorID(p);
        addSelectedColorItem(gui, colors.get(selectedColor), p);
        colors.remove(selectedColor);

        colors.values().stream().filter(color -> color.hasPerm(p)).filter(color -> color.);
    }

    private static void addColorItem(PaginatedGui gui, NameColor color, Player p) {
        colorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build());
        colorItemMeta.lore(Collections.singletonList(ColorParser.of(color.getColor() + p.name()).build()));
        colorItem.setItemMeta(colorItemMeta);
        gui.addItem(ItemBuilder.from(colorItem).asGuiItem());
    }

    private static void addSelectedColorItem(PaginatedGui gui, NameColor color, Player p) {
        selectedColorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build());
        selectedColorItemMeta.lore(Collections.singletonList(ColorParser.of(color.getColor() + p.name()).build()));
        selectedColorItem.setItemMeta(selectedColorItemMeta);
        gui.addItem(ItemBuilder.from(selectedColorItem).asGuiItem());
    }

}
