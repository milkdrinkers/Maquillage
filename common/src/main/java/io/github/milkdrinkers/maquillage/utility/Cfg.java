package io.github.milkdrinkers.maquillage.utility;

import io.github.milkdrinkers.maquillage.AbstractMaquillage;
import io.github.milkdrinkers.maquillage.config.ConfigHandler;
import io.github.milkdrinkers.maquillage.config.PluginConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public final class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link PluginConfig}
     *
     * @return the config
     */
    @NotNull
    public static PluginConfig get() {
        return AbstractMaquillage.getInstance().getConfigHandler().getConfig();
    }
}
