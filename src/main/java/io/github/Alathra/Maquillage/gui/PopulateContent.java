package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.module.namecolor.NameColor;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import io.github.Alathra.Maquillage.player.PlayerData;
import io.github.Alathra.Maquillage.player.PlayerDataHolder;
import io.github.Alathra.Maquillage.module.tag.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
        List<NameColor> colorList = colors.values().stream()
            .filter(color -> color.hasPerm(p))
            .sorted(Comparator.comparing(NameColor::getName))
            .toList();
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);

        if (playerData != null && playerData.getNameColor().isPresent()) {
            int selectedColor = playerData.getNameColor().get().getID();

            if (!colorList.isEmpty()) {
                for (NameColor color : colorList) {
                    if (color.getID() == selectedColor) {
                        addSelectedColorItem(gui, color, p, playerData);
                    } else {
                        addColorItem(gui, color, p, playerData);
                    }
                }
            }
        } else {
            if (!colorList.isEmpty()) for (NameColor color : colorList) addColorItem(gui, color, p, playerData);
        }
    }

    public static void populateTagContent(PaginatedGui gui, HashMap<Integer, Tag> tags, Player p) {
        List<Tag> tagList = tags.values().stream()
            .filter(tag -> tag.hasPerm(p))
            .sorted(Comparator.comparing(Tag::getName))
            .toList();
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);

        if (playerData != null && playerData.getTag().isPresent()) {
            int selectedTag = playerData.getTag().get().getID();

            if (!tagList.isEmpty()) {
                for (Tag tag : tagList) {
                    if (tag.getID() == selectedTag) {
                        addSelectedTagItem(gui, tag, p, playerData);
                    } else {
                        addTagItem(gui, tag, p, playerData);
                    }
                }
            }
        } else {
            if (!tagList.isEmpty()) for (Tag tag : tagList) addTagItem(gui, tag, p, playerData);
        }
    }

    private static void addColorItem(PaginatedGui gui, NameColor color, Player p, PlayerData playerData) {
        colorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build().decoration(TextDecoration.ITALIC, false));
        List<Component> loreList = new ArrayList<>();
        // Add a component to the list that contains the player's name in the color
        String tag = playerData.getTag().map(tag1 -> tag1.getTag() + "<white> ").orElse("");
        loreList.add(ColorParser.of(tag + color.getColor() + p.getName()).build().decoration(TextDecoration.ITALIC, false));
        // Add a component to the list that describes action if clicked
        loreList.add(ColorParser.of("<grey>Click this to select the color.").build());
        colorItemMeta.lore(loreList);
        colorItem.setItemMeta(colorItemMeta);
        gui.addItem(ItemBuilder.from(colorItem).asGuiItem(event -> {
            boolean success = NameColorHolder.setPlayerColor(p, color);

            if (success) {
                p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
                GuiHandler.reloadGui(GuiHandler.MaquillageGuiType.COLOR, gui, p);
            } else {
                p.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 1);
            }
        }));
    }

    private static void addSelectedColorItem(PaginatedGui gui, NameColor color, Player p, PlayerData playerData) {
        selectedColorItemMeta.displayName(ColorParser.of(color.getColor() + color.getName()).build().decoration(TextDecoration.ITALIC, false));
        String tag = playerData.getTag().map(tag1 -> tag1.getTag() + "<white> ").orElse("");
        selectedColorItemMeta.lore(Collections.singletonList(ColorParser.of(tag + color.getColor() + p.getName()).build().decoration(TextDecoration.ITALIC, false)));
        selectedColorItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        selectedColorItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        selectedColorItem.setItemMeta(selectedColorItemMeta);
        gui.addItem(ItemBuilder.from(selectedColorItem).asGuiItem(event -> gui.update()));
    }

    private static void addTagItem(PaginatedGui gui, Tag tag, Player p, PlayerData playerData) {
        tagItemMeta.displayName(ColorParser.of((tag.getName())).build().decoration(TextDecoration.ITALIC, false));
        List<Component> loreList = new ArrayList<>();
        // Add a component to the list that contains the player's name with the tag
        String color = playerData.getNameColor().map(NameColor::getColor).orElse("<white>");
        loreList.add(ColorParser.of(tag.getTag() + color + " " + p.getName()).build().decoration(TextDecoration.ITALIC, false));
        // Add a component to the list that describes action if clicked
        loreList.add(ColorParser.of("<grey>Click this to select the tag.").build());
        tagItemMeta.lore(loreList);
        tagItem.setItemMeta(tagItemMeta);
        gui.addItem(ItemBuilder.from(tagItem).asGuiItem(event -> {
            boolean success = TagHolder.setPlayerTag(p, tag);

            if (success) {
                p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
                GuiHandler.reloadGui(GuiHandler.MaquillageGuiType.TAG, gui, p);
            } else {
                p.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT, 1, 1);
            }
        }));
    }

    public static void addSelectedTagItem(PaginatedGui gui, Tag tag, Player p, PlayerData playerData) {
        selectedTagItemMeta.displayName(ColorParser.of("<white>" + tag.getName()).build().decoration(TextDecoration.ITALIC, false));
        String color = playerData.getNameColor().map(NameColor::getColor).orElse("<white>");
        selectedTagItemMeta.lore(Collections.singletonList(ColorParser.of(tag.getTag() + color + " " + p.getName()).build().decoration(TextDecoration.ITALIC, false)));
        selectedTagItem.setItemMeta(selectedTagItemMeta);
        gui.addItem(ItemBuilder.from(selectedTagItem).asGuiItem(event -> gui.update()));
    }

}
