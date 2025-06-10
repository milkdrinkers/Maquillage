package io.github.milkdrinkers.maquillage.listener;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.listener.listeners.*;

/**
 * A class to handle registration of event listeners.
 */
public class ListenerHandler implements Reloadable {
    private final Maquillage plugin;

    public ListenerHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(Maquillage plugin) {
    }

    @Override
    public void onEnable(Maquillage plugin) {
        // Register listeners here
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDataLoadedListener(), plugin);
    }

    @Override
    public void onDisable(Maquillage plugin) {
    }
}
