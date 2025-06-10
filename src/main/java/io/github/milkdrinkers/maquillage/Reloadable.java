package io.github.milkdrinkers.maquillage;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    void onLoad(Maquillage plugin);

    /**
     * On plugin enable.
     */
    void onEnable(Maquillage plugin);

    /**
     * On plugin disable.
     */
    void onDisable(Maquillage plugin);
}
