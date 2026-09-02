package io.github.milkdrinkers.maquillage.translation;

import io.github.milkdrinkers.colorparser.common.tag.CustomTags;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.AbstractMaquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import io.github.milkdrinkers.wordweaver.Translation;
import io.github.milkdrinkers.wordweaver.config.TranslationConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.nio.file.Path;
import java.util.Locale;

/**
 * A wrapper handler class for handling WordWeaver lifecycle.
 */
public class TranslationHandler implements Reloadable {
    private final ConfigHandler configHandler;

    public TranslationHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public void onEnable(AbstractMaquillage plugin) {
        Translation.initialize(buildConfig(
            plugin.getName(),
            plugin.getDataPath().resolve("lang"),
            configHandler.getConfig().language
        ));
    }

    @VisibleForTesting
    static @NotNull TranslationConfig buildConfig(
        @NotNull String pluginName,
        @NotNull Path translationDirectory,
        @NotNull String locale
    ) {
        return TranslationConfig.builder()
            .namespace("wordweaver:" + pluginName.toLowerCase(Locale.ROOT))
            .translationDirectory(translationDirectory)
            .resourcesDirectory(Path.of("lang"))
            .extractBundles(true)
            .updateBundles(true)
            .locale(locale)
            .defaultLocale("en_US")
            .componentConverter(s -> ColorParser.of(s).papi().mini().build()) // Use color parser for components by default
            .miniMessage(MiniMessage.builder()
                .editTags(builder -> builder.resolver(CustomTags.defaults()))
                .build())
            .build();
    }
}
