package io.github.milkdrinkers.maquillage.gui;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.entity.Player;

public class GuiHandler {

    public enum MaquillageGuiType {
        COLOR,
        TAG
    }

    public static PaginatedGui buildGui(MaquillageGuiType type) {
        switch (type) {
            case TAG, COLOR -> {
                return Gui.paginated()
                    .title(ColorParser.of(Translation.of("gui.title")).
                        parseMinimessagePlaceholder("cosmetic", type.toString().toLowerCase()).build())
                    .rows(6)
                    .disableItemPlace()
                    .disableItemSwap()
                    .disableItemDrop()
                    .disableItemTake()
                    .create();
            }
            default -> {
                return null;
            }
        }
    }

    public static void populateGui(MaquillageGuiType type, PaginatedGui gui, Player p) {
        switch (type) {
            case TAG -> {
                PopulateBorder.populateBorder(gui);
                PopulateContent.populateTagContent(gui, TagHolder.getInstance().cacheGet(), p);
                PopulateButtons.populateButtons(type, gui, p);
            }
            case COLOR -> {
                PopulateBorder.populateBorder(gui);
                PopulateContent.populateColorContent(gui, NameColorHolder.getInstance().cacheGet(), p);
                PopulateButtons.populateButtons(type, gui, p);
            }
        }
    }

    public static void reloadGui(MaquillageGuiType type, PaginatedGui gui, Player p) {
        switch (type) {
            case TAG -> {
                gui.clearPageItems();
                PopulateContent.populateTagContent(gui, TagHolder.getInstance().cacheGet(), p);
                PopulateButtons.populateButtons(type, gui, p);
                gui.update();
            }
            case COLOR -> {
                gui.clearPageItems();
                PopulateContent.populateColorContent(gui, NameColorHolder.getInstance().cacheGet(), p);
                PopulateButtons.populateButtons(type, gui, p);
                gui.update();
            }
        }
    }
}
