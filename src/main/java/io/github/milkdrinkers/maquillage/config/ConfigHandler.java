package io.github.milkdrinkers.maquillage.config;

import io.github.milkdrinkers.crate.internal.settings.ReloadSetting;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.crate.Config;

/**
 * A class that generates/loads {@literal &} provides access to a configuration file.
 */
public class ConfigHandler implements Reloadable {
    private final Maquillage plugin;
    private Config cfg;
    private Config databaseCfg;
    private Config importCfg;
    private Config supremeCfg;
    private Config alonsoCfg;

    /**
     * Instantiates a new Config handler.
     *
     * @param plugin the plugin instance
     */
    public ConfigHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(Maquillage plugin) {
        cfg = Config.builderConfig()
            .path(plugin.getDataFolder().toPath().resolve("config.yml"))
            .defaults(plugin.getResource("config.yml"))
            .reload(ReloadSetting.MANUALLY)
            .build(); // Create a config file from the template in our resources folder
        databaseCfg = Config.builderConfig()
            .path(plugin.getDataFolder().toPath().resolve("database.yml"))
            .defaults(plugin.getResource("database.yml"))
            .reload(ReloadSetting.MANUALLY)
            .build();

        importCfg = Config.builderConfig()
            .path(plugin.getDataFolder().toPath().resolve("import.yml"))
            .defaults(plugin.getResource("import.yml"))
            .reload(ReloadSetting.MANUALLY)
            .build();
        supremeCfg = Config.builderConfig()
            .path(plugin.getDataFolder().toPath().resolve("supreme-import.yml"))
            .defaults(plugin.getResource("supreme-import.yml"))
            .reload(ReloadSetting.MANUALLY)
            .build();
        alonsoCfg = Config.builderConfig()
            .path(plugin.getDataFolder().toPath().resolve("alonso-import.yml"))
            .defaults(plugin.getResource("alonso-import.yml"))
            .reload(ReloadSetting.MANUALLY)
            .build();
    }

    /**
     * Gets main config object.
     *
     * @return the config object
     */
    public Config getConfig() {
        return cfg;
    }

    /**
     * Gets database config object.
     *
     * @return the config object
     */
    public Config getDatabaseConfig() {
        return databaseCfg;
    }

    /**
     * Gets import config object.
     *
     * @return the config object
     */
    public Config getImportConfig() {
        return importCfg;
    }

    /**
     * Gets import config object.
     *
     * @return the config object
     */
    public Config getSupremeConfig() {
        return supremeCfg;
    }

    /**
     * Gets import config object.
     *
     * @return the config object
     */
    public Config getAlonsoConfig() {
        return alonsoCfg;
    }
}
