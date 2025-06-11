package io.github.milkdrinkers.maquillage.hook.bstats;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.hook.AbstractHook;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * A hook to interface with <a href="https://github.com/Bastian/bstats-metrics">BStats</a>.
 */
public class BStatsHook extends AbstractHook {
    private final static int BSTATS_ID = 23369; // Signup to BStats and register your new plugin here: https://bstats.org/getting-started, replace the id with you new one!
    private @Nullable Metrics hook;

    /**
     * Instantiates a new BStats hook.
     *
     * @param plugin the plugin instance
     */
    public BStatsHook(Maquillage plugin) {
        super(plugin);
    }

    @Override
    public void onEnable(Maquillage plugin) {
        // Catch startup errors for bstats
        try {
            setHook(new Metrics(getPlugin(), BSTATS_ID));
        } catch (Exception ignored) {
            setHook(null);
        }

        if (!isHookLoaded() || hook == null)
            return;

        hook.addCustomChart(new SimplePie("used_language", () -> Cfg.get().getString("translation")));

        hook.addCustomChart(new SingleLineChart("tags", () -> TagHolder.getInstance().getAllKeys().size()));

        hook.addCustomChart(new SingleLineChart("colors", () -> NameColorHolder.getInstance().getAllKeys().size()));
    }

    @Override
    public void onDisable(Maquillage plugin) {
        getHook().shutdown();
        setHook(null);
    }

    @Override
    public boolean isHookLoaded() {
        return hook != null;
    }

    /**
     * Gets BStats metrics instance. Should only be used following {@link #isHookLoaded()}.
     *
     * @return instance
     */
    public Metrics getHook() {
        if (!isHookLoaded())
            throw new IllegalStateException("Attempted to access BStats metrics instance hook when it is unavailable!");

        return hook;
    }

    /**
     * Sets the BStats metrics instance.
     *
     * @param hook The BStats metrics instance {@link Metrics}
     */
    @ApiStatus.Internal
    private void setHook(@Nullable Metrics hook) {
        this.hook = hook;
    }
}
