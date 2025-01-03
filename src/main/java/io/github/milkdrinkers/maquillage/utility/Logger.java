package io.github.milkdrinkers.maquillage.utility;


import io.github.milkdrinkers.maquillage.Maquillage;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides shorthand access to {@link Maquillage#getComponentLogger}.
 */
public class Logger {
    /**
     * Get component logger. Shorthand for:
     *
     * @return the component logger {@link Maquillage#getComponentLogger}.
     */
    @NotNull
    public static ComponentLogger get() {
        return Maquillage.getInstance().getComponentLogger();
    }
}
