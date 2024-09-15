package io.github.alathra.maquillage.utility;

import com.github.milkdrinkers.crate.Config;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.config.ConfigHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public abstract class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link Config}
     */
    @NotNull
    public static Config get() {
        return Maquillage.getInstance().getConfigHandler().getConfig();
    }
}
