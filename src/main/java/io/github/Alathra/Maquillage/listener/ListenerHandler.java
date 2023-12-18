package io.github.Alathra.Maquillage.listener;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;

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
        //instance.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin), plugin);
    }

    @Override
    public void onDisable() {
    }
}
