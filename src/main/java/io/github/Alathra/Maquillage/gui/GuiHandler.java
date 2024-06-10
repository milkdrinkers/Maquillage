package io.github.Alathra.Maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import org.bukkit.entity.Player;

public class GuiHandler {

    public enum MaquillageGuiType {
        COLOR,
        TAG
    }

    public static PaginatedGui buildGui(MaquillageGuiType type) {
        switch (type) {
            case TAG, COLOR -> {
                // TODO: make title configurable
                return Gui.paginated()
                    .title(ColorParser.of("<white>Select your " + type.toString().toLowerCase()).build())
                    .rows(6)
                    .disableItemPlace()
                    .disableItemSwap()
                    .disableItemDrop()
                    .disableItemTake()
                    .create();
            }
            default ->  {
                return null;
            }
        }
    }

    public static void populateGui(MaquillageGuiType type, PaginatedGui gui, Player p) {
        switch (type) {
            case TAG -> {
                PopulateBorder.populateBorder(gui);
                PopulateContent.populateTagContent(gui, TagHandler.loadedTags, p);
                PopulateButtons.populateButtons(type, gui, p);
            }
            case COLOR -> {
                PopulateBorder.populateBorder(gui);
                PopulateContent.populateColorContent(gui, NameColorHandler.loadedColors, p);
                PopulateButtons.populateButtons(type, gui, p);
            }
        }
    }

    public static void reloadGui(MaquillageGuiType type, PaginatedGui gui, Player p) {
        switch (type) {
            case TAG -> {
                gui.clearPageItems();
                PopulateContent.populateTagContent(gui, TagHandler.loadedTags, p);
                PopulateButtons.populateButtons(type, gui, p);
                gui.update();
            }
            case COLOR -> {
                gui.clearPageItems();
                PopulateContent.populateColorContent(gui, NameColorHandler.loadedColors, p);
                PopulateButtons.populateButtons(type, gui, p);
                gui.update();
            }
        }
    }
}
