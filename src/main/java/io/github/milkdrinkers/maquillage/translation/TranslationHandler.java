package io.github.milkdrinkers.maquillage.translation;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import io.github.milkdrinkers.wordweaver.Translation;
import io.github.milkdrinkers.wordweaver.config.TranslationConfig;

import java.nio.file.Path;

/**
 * A wrapper handler class for handling WordWeaver lifecycle.
 */
public class TranslationHandler implements Reloadable {
    private final Maquillage plugin;
    private final ConfigHandler configHandler;

    public TranslationHandler(Maquillage plugin, ConfigHandler configHandler) {
        this.plugin = plugin;
        this.configHandler = configHandler;
    }

    @Override
    public void onLoad(Maquillage plugin) {

    }

    @Override
    public void onEnable(Maquillage plugin) {
        Translation.initialize(TranslationConfig.builder() // Initialize word-weaver
            .translationDirectory(plugin.getDataFolder().toPath().resolve("lang"))
            .resourcesDirectory(Path.of("lang"))
            .extractLanguages(true)
            .updateLanguages(true)
            .language(configHandler.getConfig().get("language", "en_US"))
            .defaultLanguage("en_US")
            .componentConverter(s -> ColorParser.of(s).mini().papi().legacy().build()) // Use color parser for components by default
            .build()
        );
    }

    @Override
    public void onDisable(Maquillage plugin) {
    }
}
