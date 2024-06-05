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
        PaginatedGui gui;
        switch (type) {
            case TAG, COLOR -> {
                // TODO: make title configurable
                gui = Gui.paginated()
                    .title(ColorParser.of("<dark_grey>[<gradient:#ffff80:#00ff00>Maquillage</gradient><dark_grey>]<white> Select your " + type.toString().toLowerCase()).build())
                    .rows(6)
                    .disableItemPlace()
                    .disableItemSwap()
                    .disableItemDrop()
                    .disableItemTake()
                    .create();
                return gui;
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
        gui.close(p);
        PaginatedGui newGui = buildGui(type);
        populateGui(type, newGui, p);
        newGui.open(p);
    }
}
