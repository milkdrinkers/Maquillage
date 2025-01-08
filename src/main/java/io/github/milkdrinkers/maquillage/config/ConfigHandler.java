package io.github.milkdrinkers.maquillage.config;

import io.github.milkdrinkers.crate.Config;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;

import javax.inject.Singleton;

/**
 * A class that generates/loads {@literal &} provides access to a configuration file.
 */
@Singleton
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
    public void onLoad() {
        cfg = new Config("config", plugin.getDataFolder().getPath(), plugin.getResource("config.yml")); // Create a config file from the template in our resources folder
        databaseCfg = new Config("database", plugin.getDataFolder().getPath(), plugin.getResource("database.yml"));
        importCfg = new Config("import", plugin.getDataFolder().getPath(), plugin.getResource("import.yml"));
        supremeCfg = new Config("supreme-import", plugin.getDataFolder().getPath(), plugin.getResource("supreme-import.yml"));
        alonsoCfg = new Config("alonso-import", plugin.getDataFolder().getPath(), plugin.getResource("alonso-import.yml"));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
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

    public Config getSupremeConfig() {
        return supremeCfg;
    }

    public Config getAlonsoConfig() {
        return alonsoCfg;
    }
}
