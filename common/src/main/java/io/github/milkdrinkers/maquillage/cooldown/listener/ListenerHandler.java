package io.github.milkdrinkers.maquillage.cooldown.listener;

import io.github.milkdrinkers.maquillage.AbstractMaquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to handle registration of event listeners.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ListenerHandler implements Reloadable {
    private final AbstractMaquillage plugin;
    private final List<Listener> listeners = new ArrayList<>();

    public ListenerHandler(AbstractMaquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(AbstractMaquillage plugin) {
    }

    @Override
    public void onEnable(AbstractMaquillage plugin) {
        listeners.clear();
        listeners.add(new CooldownListener(plugin));

        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void onDisable(AbstractMaquillage plugin) {
    }
}
