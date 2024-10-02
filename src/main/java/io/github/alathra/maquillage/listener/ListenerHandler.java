package io.github.alathra.maquillage.listener;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.Reloadable;
import io.github.alathra.maquillage.listener.listeners.*;

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
        plugin.getServer().getPluginManager().registerEvents(new UpdateCheckListener(), plugin);
        if (Maquillage.getVaultHook().isVaultLoaded())
            plugin.getServer().getPluginManager().registerEvents(new VaultListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDataLoadedListener(), plugin);
    }

    @Override
    public void onDisable() {
    }
}
