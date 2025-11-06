package io.github.milkdrinkers.maquillage;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    default void onLoad(Maquillage plugin) {};

    /**
     * On plugin enable.
     */
    default void onEnable(Maquillage plugin) {}

    /**
     * On plugin disable.
     */
    default void onDisable(Maquillage plugin) {};
}
