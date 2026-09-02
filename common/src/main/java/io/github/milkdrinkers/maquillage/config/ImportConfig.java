package io.github.milkdrinkers.maquillage.config;

import io.github.milkdrinkers.maquillage.config.migration.Migration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.interfaces.meta.Exclude;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigSerializable
public class ImportConfig implements VersionedConfig {
    @Comment("Do not change this value!")
    public int configVersion = 1;

    @Override
    @Exclude
    public int configVersion() {
        return configVersion;
    }

    @Override
    @Exclude
    public @NotNull Map<Integer, Migration> migrations() {
        return Map.of(1, Migration.builder().build());
    }

    @Comment("Don't repeat this key for new tags, simply add new sections under it.")
    public Map<String, TagEntry> tags = defaultTags();

    @Comment("Don't repeat this key for new namecolors, simply add new sections under it.")
    public Map<String, NameColorEntry> namecolors = defaultNameColors();

    @ConfigSerializable
    public static class TagEntry {
        @Comment("What the tag will actually be in-game.")
        public String tag = "";

        @Comment("The label displayed in the GUI where players pick their tags.")
        public String label = "";

        @Comment("Final permission node is \"maquillage.tag.<your input>\". Leave blank for a permissionless tag.")
        public String permission = "";

        @Comment("Higher weights get listed first in the GUI.")
        public int weight = 0;

        public TagEntry() {
        }

        public TagEntry(String tag, String label, String permission, int weight) {
            this.tag = tag;
            this.label = label;
            this.permission = permission;
            this.weight = weight;
        }
    }

    @ConfigSerializable
    public static class NameColorEntry {
        @Comment("What the namecolor will actually be in-game.")
        public String color = "";

        @Comment("The label displayed in the GUI where players pick their colors.")
        public String label = "";

        @Comment("Final permission node is \"maquillage.namecolor.<your input>\". Leave blank for a permissionless namecolor.")
        public String permission = "";

        @Comment("Higher weights get listed first in the GUI.")
        public int weight = 0;

        public NameColorEntry() {
        }

        public NameColorEntry(String color, String label, String permission, int weight) {
            this.color = color;
            this.label = label;
            this.permission = permission;
            this.weight = weight;
        }
    }

    private static Map<String, TagEntry> defaultTags() {
        final Map<String, TagEntry> map = new LinkedHashMap<>();
        map.put("example", new TagEntry("<dark_grey>[<green>Example<dark_grey>]", "<grey>Example tag", "", 0));
        map.put("another-example", new TagEntry("<dark_grey>[<green>Another example<dark_grey>]", "<grey>Another example tag", "anotherexample", 100));
        return map;
    }

    private static Map<String, NameColorEntry> defaultNameColors() {
        final Map<String, NameColorEntry> map = new LinkedHashMap<>();
        map.put("example", new NameColorEntry("<aqua>", "<aqua>Example color", "", 0));
        map.put("another-example", new NameColorEntry("<green>", "<green>Another example color", "green", 100));
        return map;
    }
}
