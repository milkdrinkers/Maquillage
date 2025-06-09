package io.github.milkdrinkers.maquillage.listener.listeners;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.translation.Translation;
import io.github.milkdrinkers.maquillage.updatechecker.SemanticVersion;
import io.github.milkdrinkers.maquillage.updatechecker.UpdateChecker;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Sends an update message to operators if there's a plugin update available
 */
public class UpdateCheckListener implements Listener {
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Maquillage.getInstance().getUpdateChecker().isLatest())
            return;

        if (!Cfg.get().getOrDefault("update-checker.enable", true) || !Cfg.get().getOrDefault("update-checker.op", true))
            return;

        if (!e.getPlayer().isOp())
            return;

        String pluginName = Maquillage.getInstance().getUpdateChecker().getPluginName();
        SemanticVersion latestVersion = Maquillage.getInstance().getUpdateChecker().getLatestVersion();
        SemanticVersion currentVersion = Maquillage.getInstance().getUpdateChecker().getCurrentVersion();

        if (latestVersion == null || currentVersion == null)
            return;

        e.getPlayer().sendMessage(
            ColorParser.of(Translation.of("update-checker.update-found-player"))
                .with("plugin_name", pluginName)
                .with("version_current", currentVersion.getVersionFull())
                .with("version_latest", latestVersion.getVersionFull())
                .with("download_link", UpdateChecker.LATEST_RELEASE)
                .build()
        );
    }
}
