package io.github.milkdrinkers.maquillage.gui;

import dev.triumphteam.gui.builder.item.PaperItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class PopulateBorder {

    public static void populateBorder(PaginatedGui gui) {
        GuiItem greyGlassPane = PaperItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text("")).asGuiItem();

        gui.getFiller().fillBorder(greyGlassPane);
    }

}
