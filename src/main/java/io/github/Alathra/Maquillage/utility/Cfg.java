package io.github.Alathra.Maquillage.utility;

import com.github.milkdrinkers.Crate.Config;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.config.ConfigHandler;
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
