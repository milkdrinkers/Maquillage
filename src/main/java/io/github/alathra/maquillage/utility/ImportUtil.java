package io.github.alathra.maquillage.utility;

import com.github.milkdrinkers.crate.Config;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;

import java.util.Map;

public class ImportUtil {

    public static Config get() {
        return Maquillage.getInstance().getConfigHandler().getImportConfig();
    }

    public static Map<?, ?> getTagMap() {
        if (get() == null)
            return null;
        return get().getMap("tags");
    }

    public static Map<?, ?> getNamecolorMap() {
        if (get() == null)
            return null;
        return get().getMap("namecolors");
    }

    public static int getTagAmount() {
        if (getTagMap() == null)
            return 0;
        return getTagMap().keySet().size();
    }

    public static int getNamecolorAmount() {
        if (getNamecolorMap() == null)
            return 0;
        return getNamecolorMap().keySet().size();
    }

    /**
     * Convenience method used in conversation for import command.
     *
     * @return a string with the amount of tags and namecolors to be imported.
     */
    public static String getTagAndNamecolorAmounts() {
        return getTagAmount() + " tags and " + getNamecolorAmount() + " namecolors";
    }

    /**
     * Adds all tags in the import file.
     *
     */
    public static void addAllTags() {
        if (getTagMap() == null || getTagMap().isEmpty())
            return;
        getTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            get().getString("tags." + key + ".tag"),
            get().getString("tags." + key + ".permission-node"),
            get().getString("tags." + key + ".gui-label")));
    }

    public static void addAllNamecolors() {
        if (getNamecolorMap() == null || getNamecolorMap().isEmpty())
            return;
        getNamecolorMap().keySet().forEach(key -> NameColorHolder.getInstance().add(
            get().getString("namecolors." + key + ".color"),
            get().getString("namecolors." + key + ".permission-node"),
            get().getString("namecolors." + key + ".gui-label")));
    }

}
