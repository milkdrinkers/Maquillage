package io.github.milkdrinkers.maquillage.utility;

import io.github.milkdrinkers.crate.Config;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;

import java.util.Map;

public class ImportUtil {
    private static Config getImport() {
        return Maquillage.getInstance().getConfigHandler().getImportConfig();
    }

    private static Map<?, ?> getTagMap() {
        if (getImport() == null)
            return null;
        return getImport().getMap("tags");
    }

    private static Map<?, ?> getNamecolorMap() {
        if (getImport() == null)
            return null;
        return getImport().getMap("namecolors");
    }

    public static int getTagAmount() {
        if (getTagMap() == null)
            return 0;
        return getTagMap().size();
    }

    public static int getNamecolorAmount() {
        if (getNamecolorMap() == null)
            return 0;
        return getNamecolorMap().size();
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
     */
    public static void addAllTags() {
        if (getTagMap() == null || getTagMap().isEmpty())
            return;
        getTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            getImport().getString("tags." + key + ".tag"),
            getImport().getString("tags." + key + ".permission-node"),
            getImport().getString("tags." + key + ".gui-label"),
            0));
    }

    public static void addAllNamecolors() {
        if (getNamecolorMap() == null || getNamecolorMap().isEmpty())
            return;
        getNamecolorMap().keySet().forEach(key -> NameColorHolder.getInstance().add(
            getImport().getString("namecolors." + key + ".color"),
            getImport().getString("namecolors." + key + ".permission-node"),
            getImport().getString("namecolors." + key + ".gui-label"),
            0));
    }

    public static void addSupremeTags() {
        if (getSupremeTagMap() == null || getSupremeTagMap().isEmpty())
            return;
        getSupremeTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            getSupreme().getString("tags." + key + ".tag"),
            getSupreme().getString("tags." + key + ".permission"),
            getSupreme().getString("tags." + key + ".displayname"),
            0
        ));
    }

    public static void addAlonsoTags() {
        if (getAlonsoTagMap() == null || getAlonsoTagMap().isEmpty())
            return;
        getAlonsoTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            getAlonso().getString("Tags." + key + ".Tag"),
            getAlonso().getString("Tags." + key + ".Permission"),
            getAlonso().getString("Tags." + key + ".Displayname"),
            0
        ));
    }

}
