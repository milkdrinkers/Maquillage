package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PopulateContent {

    static ItemStack colorItem = new ItemStack(Material.GRAY_DYE);
    static ItemMeta colorItemMeta = colorItem.getItemMeta();
    static ItemStack selectedColorItem = new ItemStack(Material.LIME_DYE);
    static ItemMeta selectedColorItemMeta = selectedColorItem.getItemMeta();

    public static void populateColorContent(PaginatedGui gui, HashMap<Integer, NameColor> colors, Player p) {
        List<NameColor> colorList;
        if (NameColorHandler.doesPlayerHaveColor(p)) {
            int selectedColor = NameColorHandler.getPlayerColorID(p);
            addSelectedColorItem(gui, colors.get(selectedColor), p);

            colorList = colors.values().stream()
                .filter(color -> color.hasPerm(p))
                .filter(color -> color.getID() != selectedColor).toList();
        } else {
            colorList = colors.values().stream()
                .filter(color -> color.hasPerm(p)).toList();
        }
        for (NameColor color : colorList) {
            addColorItem(gui, color, p);
        }
    }

    private static void addColorItem(PaginatedGui gui, NameColor color, Player p) {
        colorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build());
        // TODO: fix lore text
        List<Component> loreList = new ArrayList<>();
        // Add a component to the list that contains the player's name in the color
        loreList.add(ColorParser.of(color.getColor() + p.getName()).build());
        // Add a component to the list that describes action if clicked
        loreList.add(ColorParser.of("<grey>Click this to select the color.").build());
        colorItemMeta.lore(loreList);
        colorItem.setItemMeta(colorItemMeta);
        gui.addItem(ItemBuilder.from(colorItem).asGuiItem(event -> {
            NameColorHandler.setPlayerColor(p, color);
            // TODO: test with multiple pages
            gui.clearPageItems();
            populateColorContent(gui, NameColorHandler.loadedColors, p);
            gui.update();
        }));
    }

    private static void addSelectedColorItem(PaginatedGui gui, NameColor color, Player p) {
        selectedColorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build());
        selectedColorItemMeta.lore(Collections.singletonList(ColorParser.of(color.getColor() + p.getName()).build()));
        selectedColorItem.setItemMeta(selectedColorItemMeta);
        gui.addItem(ItemBuilder.from(selectedColorItem).asGuiItem(event -> {
            gui.update();
        }));
    }

}
