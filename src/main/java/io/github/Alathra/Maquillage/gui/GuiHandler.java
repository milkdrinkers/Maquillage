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
        TAG,
        ADMIN
    }

    public static PaginatedGui buildGui(MaquillageGuiType type) {
        PaginatedGui gui;
        switch (type) {
            case ADMIN -> {
                // TODO: add admin gui
                return null;
            }
            case TAG, COLOR -> {
                // TODO: make title configurable
                gui = Gui.paginated()
                    .title(ColorParser.of("<dark_red>[<dark_aqua>Maquillage<dark_red>] Select your " + type.toString().toLowerCase()).build())
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
            case ADMIN -> {
                return;
            }
            case TAG -> {
                PopulateBorder.populateBorder(gui);
                PopulateContent.populateTagContent(gui, TagHandler.loadedTags, p);
                PopulateButtons.populateButtons(gui, p, type);
            }
            case COLOR -> {
                PopulateBorder.populateBorder(gui);
                PopulateContent.populateColorContent(gui, NameColorHandler.loadedColors, p);
                PopulateButtons.populateButtons(gui, p, type);
            }
        }
    }
}
