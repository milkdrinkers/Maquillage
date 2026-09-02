package io.github.milkdrinkers.maquillage.hook.essentialsx;

import com.earth2me.essentials.Essentials;
import io.github.milkdrinkers.maquillage.AbstractMaquillage;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.hook.AbstractHook;
import io.github.milkdrinkers.maquillage.hook.Hook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A hook to interface with EssentialsX.
 */
public class EssentialsHook extends AbstractHook {
    private @Nullable Essentials hook;

    /**
     * Instantiates a new Essentials hook.
     *
     * @param plugin the plugin instance
     */
    public EssentialsHook(Maquillage plugin) {
        super(plugin);
    }

    @Override
    public void onEnable(AbstractMaquillage plugin) {
        if (!isPluginEnabled(Hook.Essentials.getPluginName()))
            return;

        setHook((Essentials) Bukkit.getPluginManager().getPlugin(Hook.Essentials.getPluginName()));
    }

    @Override
    public void onDisable(AbstractMaquillage plugin) {
        setHook(null);
    }

    @Override
    public boolean isHookLoaded() {
        return hook != null;
    }

    /**
     * Gets EssentialsX instance. Should only be used following {@link #isHookLoaded()}.
     *
     * @return instance
     */
    public Essentials getHook() {
        if (!isHookLoaded())
            throw new IllegalStateException("Attempted to access Essentials hook when it is unavailable!");

        return hook;
    }

    /**
     * Sets the Essentials instance.
     *
     * @param hook The Essentials instance {@link Essentials}
     */
    @ApiStatus.Internal
    private void setHook(@Nullable Essentials hook) {
        this.hook = hook;
    }
}
