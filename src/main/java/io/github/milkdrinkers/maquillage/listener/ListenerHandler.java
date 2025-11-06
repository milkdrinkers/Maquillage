package io.github.milkdrinkers.maquillage.listener;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.listener.listeners.MessageReceivedListener;
import io.github.milkdrinkers.maquillage.listener.listeners.PlayerDataLoadedListener;
import io.github.milkdrinkers.maquillage.listener.listeners.PlayerJoinListener;
import io.github.milkdrinkers.maquillage.listener.listeners.PlayerQuitListener;

/**
 * A class to handle registration of event listeners.
 */
public class ListenerHandler implements Reloadable {
    private final Maquillage plugin;

    public ListenerHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable(Maquillage plugin) {
        // Register listeners here
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDataLoadedListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MessageReceivedListener(), plugin);
    }

}
