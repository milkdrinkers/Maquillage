package io.github.alathra.maquillage.utility;

import com.github.milkdrinkers.crate.Config;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;

import java.util.Map;

public class ImportUtil {

    public ImportUtil() {

    }

    public static ImportUtil getInstance() {
        return Maquillage.getInstance().getImportUtil();
    }

    public Config get() {
        return Maquillage.getInstance().getConfigHandler().getImportConfig();
    }

    public Map<?, ?> getTagMap() {
        return get().getMap("tags");
    }

    public Map<?, ?> getNamecolorMap() {
        return get().getMap("namecolors");
    }

    public int getTagAmount() {
        if (getTagMap() != null)
            return getTagMap().keySet().size();
        return 0;
    }

    public int getNamecolorAmount() {
        if (getNamecolorMap() != null)
            return getNamecolorMap().keySet().size();
        return 0;
    }

    /**
     * Convenience method used in conversation for import command.
     *
     * @return a string with the amount of tags and namecolors to be imported.
     */
    public String getTagAndNamecolorAmounts() {
        return getTagAmount() + " tags and " + getNamecolorAmount() + " namecolors";
    }

    /**
     * Adds all tags in the import file.
     *
     */
    public void addAllTags() {
        if (getTagMap() == null || getTagMap().isEmpty())
            return;
        getTagMap().keySet().forEach(key -> TagHolder.getInstance().add(
            get().getString("tags." + key + ".tag"),
            get().getString("tags." + key + ".permission-node"),
            get().getString("tags." + key + ".gui-label")));
    }

    public void addAllNamecolors() {
        if (getNamecolorMap() == null || getNamecolorMap().isEmpty())
            return;
        getNamecolorMap().keySet().forEach(key -> NameColorHolder.getInstance().add(
            get().getString("namecolors." + key + ".color"),
            get().getString("namecolors." + key + ".permission-node"),
            get().getString("namecolors." + key + ".gui-label")));
    }

}
