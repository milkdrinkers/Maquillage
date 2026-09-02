package io.github.milkdrinkers.maquillage.config;

import io.github.milkdrinkers.maquillage.AbstractMaquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.config.loading.ConfigLoader;
import io.github.milkdrinkers.maquillage.config.typeserializer.StringListSerializer;
import io.github.milkdrinkers.maquillage.config.typeserializer.StringObjectMapSerializer;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * A class that generates/loads {@literal &} provides access to a configuration file.
 */
public class ConfigHandler implements Reloadable {
    private final AbstractMaquillage plugin;
    private final Path configDir;
    private final Logger logger;

    private PluginConfig cfg;
    private DatabaseConfig databaseCfg;
    private ImportConfig importCfg;

    /**
     * Instantiates a new Config handler.
     *
     * @param plugin the plugin instance
     */
    public ConfigHandler(AbstractMaquillage plugin) {
        this.plugin = plugin;
        this.configDir = plugin.getDataFolder().toPath();
        this.logger = plugin.getComponentLogger();
    }

    public ConfigHandler(AbstractMaquillage plugin, Path configDir, Logger logger) {
        this.plugin = plugin;
        this.configDir = configDir;
        this.logger = logger;
    }

    @Override
    public void onLoad(AbstractMaquillage plugin) {
        cfg = new ConfigLoader()
            .withLogger(logger)
            .withDirectory()
            .withPath(configDir.resolve("config.yml"))
            .withHeader("")
            .build(PluginConfig.class);

        databaseCfg = new ConfigLoader()
            .withLogger(logger)
            .withDirectory()
            .withPath(configDir.resolve("database.yml"))
            .withHeader("")
            .withSerializer(b -> {
                b.registerExact(StringListSerializer.TYPE_TOKEN, StringListSerializer.INSTANCE)
                    .registerExact(StringObjectMapSerializer.TYPE_TOKEN, StringObjectMapSerializer.INSTANCE);
            })
            .build(DatabaseConfig.class);

        importCfg = new ConfigLoader()
            .withLogger(logger)
            .withDirectory()
            .withPath(configDir.resolve("data/import.yml"))
            .withHeader(
                """
                    This file can be used to import multiple cosmetics by running the in-game command "/maquillage import".
                    
                    You can add any number of cosmetics at once using this, as long as you follow the provided pattern.
                    To add a new cosmetic, fill in new sections like the examples below.
                    
                    After loading the cosmetics this file can safely be cleared.
                    If you run the command again without clearing the file, you will end up with duplicate cosmetics.
                    Removing cosmetics from this file will not remove them in-game. To remove them in-game, you can either manually modify the database or run commands.
                    
                    Cosmetic strings and labels support MiniMessage styling.
                    You can find information about and documentation for MiniMessage here: https://docs.papermc.io/adventure/minimessage/
                    You can find a Web-UI that can be used to test MiniMessage styling here: https://webui.advntr.dev/
                    """
            )
            .build(ImportConfig.class);
    }

    /**
     * Gets main config object.
     *
     * @return the config object
     */
    public PluginConfig getConfig() {
        return cfg;
    }

    /**
     * Gets database config object.
     *
     * @return the config object
     */
    public DatabaseConfig getDatabaseConfig() {
        return databaseCfg;
    }

    /**
     * Gets import config object.
     *
     * @return the config object
     */
    public ImportConfig getImportConfig() {
        return importCfg;
    }
}
