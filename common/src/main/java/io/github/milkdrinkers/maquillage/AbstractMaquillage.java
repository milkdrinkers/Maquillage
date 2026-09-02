package io.github.milkdrinkers.maquillage;

import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMaquillage extends JavaPlugin {
    private static AbstractMaquillage instance;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static AbstractMaquillage getInstance() {
        return AbstractMaquillage.instance;
    }

    AbstractMaquillage() {
        AbstractMaquillage.instance = this;
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    public abstract @NotNull ConfigHandler getConfigHandler();
}
