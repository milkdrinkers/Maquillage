package io.github.milkdrinkers.maquillage;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    default void onLoad(AbstractMaquillage plugin) {
    }

    /**
     * On plugin enable.
     */
    default void onEnable(AbstractMaquillage plugin) {
    }

    /**
     * On plugin disable.
     */
    default void onDisable(AbstractMaquillage plugin) {
    }

}
