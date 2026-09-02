package io.github.milkdrinkers.maquillage.hook.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.hook.AbstractHook;
import io.github.milkdrinkers.maquillage.hook.Hook;
import org.jetbrains.annotations.NotNull;

/**
 * A hook to interface with PacketEvents.
 */
public class PacketEventsHook extends AbstractHook {
    /**
     * Instantiates a new PacketEvents hook.
     *
     * @param plugin the plugin instance
     */
    public PacketEventsHook(Maquillage plugin) {
        super(plugin);
    }

    @Override
    public boolean isHookLoaded() {
        if (!isPluginEnabled(Hook.PacketEvents.getPluginName()))
            return false;

        final PacketEventsAPI<?> api = PacketEvents.getAPI();
        return api != null && api.isLoaded();
    }

    public @NotNull PacketEventsAPI<?> getAPI() {
        if (!isHookLoaded())
            throw new IllegalStateException("Attempted to access the PacketEvents hook when it is unavailable!");

        return PacketEvents.getAPI();
    }
}
