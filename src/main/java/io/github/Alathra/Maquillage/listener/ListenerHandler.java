package io.github.Alathra.Maquillage.listener;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;
import io.github.Alathra.Maquillage.listener.listeners.EssentialsNicknameListener;
import io.github.Alathra.Maquillage.listener.listeners.PlayerJoinListener;
import io.github.Alathra.Maquillage.listener.listeners.PlayerQuitListener;

/**
 * A class to handle registration of event listeners.
 */
public class ListenerHandler implements Reloadable {
    private final Maquillage plugin;

    public ListenerHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        // Register listeners here
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new EssentialsNicknameListener(), plugin);
    }

    @Override
    public void onDisable() {
    }
}
