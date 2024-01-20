package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.Tag;
import io.github.Alathra.Maquillage.tag.TagHandler;
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
    static ItemStack tagItem = new ItemStack(Material.NAME_TAG);
    static ItemMeta tagItemMeta = tagItem.getItemMeta();
    static ItemStack selectedTagItem = new ItemStack(Material.ENCHANTED_BOOK);
    static ItemMeta selectedTagItemMeta = selectedTagItem.getItemMeta();

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

        if(!colorList.isEmpty()) {
            for (NameColor color : colorList) {
                addColorItem(gui, color, p);
            }
        }
    }

    public static void populateTagContent(PaginatedGui gui, HashMap<Integer, Tag> tags, Player p) {
        List<Tag> tagList;
        if (TagHandler.doesPlayerHaveTag(p)) {
            int selectedTag = TagHandler.getPlayerTagID(p);
            addSelectedTagItem(gui, tags.get(selectedTag), p);

            tagList = tags.values().stream()
                .filter(tag -> tag.hasPerm(p))
                .filter(tag -> tag.getID() != selectedTag).toList();
        } else {
            tagList = tags.values().stream()
                .filter(tag -> tag.hasPerm(p)).toList();
        }

        if (!tagList.isEmpty()) {
            for (Tag tag : tagList) {
                addTagItem(gui, tag, p);
            }
        }
    }

    private static void addColorItem(PaginatedGui gui, NameColor color, Player p) {
        colorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build());
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
            GuiHandler.reloadGui(GuiHandler.MaquillageGuiType.COLOR, gui, p);
        }));
    }

    private static void addSelectedColorItem(PaginatedGui gui, NameColor color, Player p) {
        selectedColorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build());
        selectedColorItemMeta.lore(Collections.singletonList(ColorParser.of(color.getColor() + p.getName()).build()));
        selectedColorItem.setItemMeta(selectedColorItemMeta);
        gui.addItem(ItemBuilder.from(selectedColorItem).asGuiItem(event -> gui.update()));
    }

    private static void addTagItem(PaginatedGui gui, Tag tag, Player p) {
        tagItemMeta.displayName(ColorParser.of((tag.getName())).build());
        List<Component> loreList = new ArrayList<>();
        // Add a component to the list that contains the player's name with the tag
        String color = "<white>";
        if(NameColorHandler.doesPlayerHaveColor(p))
            color = NameColorHandler.getPlayerColorString(p);
        loreList.add(ColorParser.of(tag.getTag() + color + " " + p.getName()).build());
        // Add a component to the list that describes action if clicked
        loreList.add(ColorParser.of("<grey>Click this to select the tag.").build());
        tagItemMeta.lore(loreList);
        tagItem.setItemMeta(tagItemMeta);
        gui.addItem(ItemBuilder.from(tagItem).asGuiItem(event -> {
            TagHandler.setPlayerTag(p, tag);
            GuiHandler.reloadGui(GuiHandler.MaquillageGuiType.TAG, gui, p);
        }));
    }

    public static void addSelectedTagItem(PaginatedGui gui, Tag tag, Player p) {
        selectedTagItemMeta.displayName(ColorParser.of("<white>" + tag.getName()).build());
        String color = "<white>";
        if(NameColorHandler.doesPlayerHaveColor(p))
            color = NameColorHandler.getPlayerColorString(p);
        selectedTagItemMeta.lore(Collections.singletonList(ColorParser.of(tag.getTag() + color + " " + p.getName()).build()));
        selectedTagItem.setItemMeta(selectedTagItemMeta);
        gui.addItem(ItemBuilder.from(selectedTagItem).asGuiItem(event -> gui.update()));
    }

}
