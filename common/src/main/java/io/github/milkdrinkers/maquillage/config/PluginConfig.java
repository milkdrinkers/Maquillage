package io.github.milkdrinkers.maquillage.config;

import io.github.milkdrinkers.maquillage.config.exception.ConfigValidationException;
import io.github.milkdrinkers.maquillage.config.migration.Migration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.interfaces.meta.Exclude;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Map;

@ConfigSerializable
public class PluginConfig implements VersionedConfig {
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
        return Map.of(
            1, Migration.builder()
                .rename(NodePath.path("update-checker", "enable"), "enabled")
                .build()
        );
    }

    @Override
    @Exclude
    public void validate() throws ConfigValidationException {
        if (language == null || language.isBlank())
            throw new ConfigValidationException("language must not be empty");

        if (module.nickname.length < 1 || module.nickname.length > 255)
            throw new ConfigValidationException("module.nickname.length must be between 1 and 255");
    }

    @Comment("Update Checker Settings")
    public UpdateChecker updateChecker = new UpdateChecker();

    @ConfigSerializable
    public static class UpdateChecker {
        @Comment("Should the plugin check for plugin updates on startup?")
        public boolean enabled = true;

        @Comment("Send update notifications to the console?")
        public boolean console = true;

        @Comment("Send update notifications to opped players on join?")
        public boolean op = true;
    }

    @Comment("Language, specify the language file to use, for example `en_US` which will load `/lang/en_US.json`")
    public String language = "en_US";

    @Comment("Enable or disable specific modules")
    public Module module = new Module();

    @ConfigSerializable
    public static class Module {
        public Tag tag = new Tag();
        public NameColor namecolor = new NameColor();
        public Nickname nickname = new Nickname();

        @ConfigSerializable
        public static class Tag {
            public boolean enabled = true;
        }

        @ConfigSerializable
        public static class NameColor {
            public boolean enabled = true;
        }

        @ConfigSerializable
        public static class Nickname {
            public boolean enabled = true;

            public Prefix prefix = new Prefix();

            @Comment("Sets the displayname to the nickname. Disable this if you have another plugin which modifies the displayname")
            public boolean setDisplayname = true;

            @Comment("Sets the listname (shown in the tab-menu) for the player. Disable this if you have another plugin which modifies the listname")
            public boolean setListname = true;

            @Comment("The maximum allowed length for nicknames. Allowed range: 1-255")
            public int length = 20;

            @ConfigSerializable
            public static class Prefix {
                public boolean enabled = true;
                public String string = "";
            }
        }
    }
}
