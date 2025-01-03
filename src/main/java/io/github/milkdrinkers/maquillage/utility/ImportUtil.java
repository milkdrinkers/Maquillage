package io.github.milkdrinkers.maquillage.utility;

import com.github.milkdrinkers.crate.Config;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;

import java.util.Map;

public class ImportUtil {

    public static Config getImport() {
        return Maquillage.getInstance().getConfigHandler().getImportConfig();
    }

    public static Config getSupreme() {
        return Maquillage.getInstance().getConfigHandler().getSupremeConfig();
    }

    public static Config getAlonso() {
        return Maquillage.getInstance().getConfigHandler().getAlonsoConfig();
    }

    public static Map<?, ?> getTagMap() {
        if (getImport() == null)
            return null;
        return getImport().getMap("tags");
    }

    public static Map<?, ?> getSupremeTagMap() {
        if (getSupreme() == null)
            return null;
        return getSupreme().getMap("tags");
    }

    public static Map<?, ?> getAlonsoTagMap() {
        if (getAlonso() == null)
            return null;
        return getAlonso().getMap("tags");
    }

    public static Map<?, ?> getNamecolorMap() {
        if (getImport() == null)
            return null;
        return getImport().getMap("namecolors");
    }

    public static int getTagAmount() {
        if (getTagMap() == null)
            return 0;
        return getTagMap().keySet().size();
    }

    public static int getSupremeTagAmount() {
        if (getSupremeTagMap() == null)
            return 0;
        return getSupremeTagMap().keySet().size();
    }

    public static int getAlonsoTagAmount() {
        if (getAlonsoTagMap() == null)
            return 0;
        return getAlonsoTagMap().keySet().size();
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
            getImport().getString("tags." + key + ".tag"),
            getImport().getString("tags." + key + ".permission-node"),
            getImport().getString("tags." + key + ".gui-label")));
    }

    public static void addAllNamecolors() {
        if (getNamecolorMap() == null || getNamecolorMap().isEmpty())
            return;
        getNamecolorMap().keySet().forEach(key -> NameColorHolder.getInstance().add(
            getImport().getString("namecolors." + key + ".color"),
            getImport().getString("namecolors." + key + ".permission-node"),
            getImport().getString("namecolors." + key + ".gui-label")));
    }

    public static void addSupremeTags() {
        if (getSupremeTagMap() == null || getSupremeTagMap().isEmpty())
            return;
        getSupremeTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            getSupreme().getString("tags." + key + ".tag"),
            getSupreme().getString("tags." + key + ".permission"),
            getSupreme().getString("tags." + key + ".displayname")
        ));
    }

    public static void addAlonsoTags() {
        if (getAlonsoTagMap() == null || getAlonsoTagMap().isEmpty())
            return;
        getAlonsoTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            getAlonso().getString("Tags." + key + ".Tag"),
            getAlonso().getString("Tags." + key + ".Permission"),
            getAlonso().getString("Tags." + key + ".Displayname")
        ));
    }

}
