package io.github.milkdrinkers.maquillage.utility;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.config.ImportConfig;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;

import java.util.Map;

public class ImportUtil {
    private static ImportConfig getImport() {
        return Maquillage.getInstance().getConfigHandler().getImportConfig();
    }

    private static Map<String, ImportConfig.TagEntry> getTagMap() {
        final ImportConfig cfg = getImport();
        return cfg == null ? Map.of() : cfg.tags;
    }

    private static Map<String, ImportConfig.NameColorEntry> getNamecolorMap() {
        final ImportConfig cfg = getImport();
        return cfg == null ? Map.of() : cfg.namecolors;
    }

    public static int getTagAmount() {
        return getTagMap().size();
    }

    public static int getNamecolorAmount() {
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
    public static void importTags() {
        getTagMap().values().forEach(entry -> TagHolder.getInstance().add(
            entry.tag,
            entry.permission,
            entry.label,
            entry.weight
        ));
    }

    public static void importColors() {
        getNamecolorMap().values().forEach(entry -> NameColorHolder.getInstance().add(
            entry.color,
            entry.permission,
            entry.label,
            entry.weight
        ));
    }
}
