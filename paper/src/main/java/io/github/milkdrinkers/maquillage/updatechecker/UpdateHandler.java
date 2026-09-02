package io.github.milkdrinkers.maquillage.updatechecker;

import io.github.milkdrinkers.javasemver.Version;
import io.github.milkdrinkers.javasemver.exception.VersionParseException;
import io.github.milkdrinkers.maquillage.AbstractMaquillage;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import io.github.milkdrinkers.maquillage.utility.Logger;
import io.github.milkdrinkers.versionwatch.Platform;
import io.github.milkdrinkers.versionwatch.VersionWatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Run update checks against your release platform.
 */
public class UpdateHandler implements Reloadable {
    private final static String GITHUB_USER = "milkdrinkers"; // The GitHub user/organization name
    private final static String GITHUB_REPO = "Maquillage"; // The GitHub repository

    private final VersionWatcher watcher;

    public UpdateHandler(Maquillage plugin) {
        this.watcher = VersionWatcher.builder()
            .withPlatform(Platform.GitHub)
            .withVersion(getCurrentVersion(plugin))
            .withResourceOwner(GITHUB_USER)
            .withResourceSlug(GITHUB_REPO)
            .withAgent(plugin.getName() + getCurrentVersion(plugin))
            .build();
    }

    /**
     * On plugin enable.
     */
    @Override
    public void onEnable(AbstractMaquillage plugin) {
        final boolean shouldLog = Cfg.get().updateChecker.enabled && Cfg.get().updateChecker.console;

        // Fetch the latest version and send message to console
        watcher.fetchLatestAsync().thenAccept(version -> {
            if (version == null)
                return;

            if (!shouldLog)
                return;

            if (watcher.isLatest()) {
                Logger.get().info(Component.translatable(
                    "maquillage.update-checker.running-latest",
                    Argument.string("plugin_name", plugin.getName())
                ));
            } else {
                Logger.get().info(Component.translatable(
                    "maquillage.update-checker.update-found-console",
                    Argument.string("plugin_name", plugin.getName()),
                    Argument.string("version_current", watcher.getCurrentVersion().getVersionFull()),
                    Argument.string("version_latest", version.getVersionFull()),
                    Argument.tagResolver(Placeholder.parsed("download_link", watcher.getDownloadURL()))
                ));
            }
        }).exceptionally(throwable -> {
            if (shouldLog)
                Logger.get().warn(Component.translatable(
                    "maquillage.update-checker.update-failed",
                    Argument.string("error", throwable.getMessage())
                ));
            return null;
        });

        // Register version check message listener for opped player joins
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            @SuppressWarnings("unused")
            public void onPlayerJoin(PlayerJoinEvent e) {
                final Player p = e.getPlayer();

                if (watcher.isLatest())
                    return;

                if (!Cfg.get().updateChecker.enabled || !Cfg.get().updateChecker.op)
                    return;

                if (!p.isOp())
                    return;

                if (watcher.getLatestVersion() == null)
                    return;

                p.sendMessage(Component.translatable(
                    "maquillage.update-checker.update-found-player",
                    Argument.string("plugin_name", plugin.getName()),
                    Argument.string("version_current", watcher.getCurrentVersion().getVersionFull()),
                    Argument.string("version_latest", watcher.getLatestVersion().getVersionFull()),
                    Argument.tagResolver(Placeholder.parsed("download_link", watcher.getDownloadURL()))
                ));
            }
        }, plugin);
    }

    /**
     * Get the current version of the plugin or 0.0.1 if it can't be found.
     *
     * @param plugin the plugin instance
     * @return the current version of the plugin
     */
    private Version getCurrentVersion(Maquillage plugin) {
        try {
            return Version.parseLoose(plugin.getPluginMeta().getVersion());
        } catch (VersionParseException e) {
            return Version.builder()
                .withMajor(0)
                .withMinor(0)
                .withPatch(1)
                .build();
        }
    }
}
