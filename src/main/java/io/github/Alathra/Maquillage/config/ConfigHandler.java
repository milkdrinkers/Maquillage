package io.github.Alathra.Maquillage.config;

import com.github.milkdrinkers.Crate.Config;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;

import javax.inject.Singleton;

/**
 * A class that generates/loads & provides access to a configuration file.
 */
@Singleton
public class ConfigHandler implements Reloadable {
    private final Maquillage plugin;
    private Config cfg;

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
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    /**
     * Gets examplePlugin config object.
     *
     * @return the examplePlugin config object
     */
    public Config getConfig() {
        return cfg;
    }
}
