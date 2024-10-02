package io.github.alathra.maquillage.listener.listeners;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.translation.Translation;
import io.github.alathra.maquillage.updatechecker.SemanticVersion;
import io.github.alathra.maquillage.updatechecker.UpdateChecker;
import io.github.alathra.maquillage.utility.Cfg;
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
                .parseMinimessagePlaceholder("plugin_name", pluginName)
                .parseMinimessagePlaceholder("version_current", currentVersion.getVersionFull())
                .parseMinimessagePlaceholder("version_latest", latestVersion.getVersionFull())
                .parseMinimessagePlaceholder("download_link", UpdateChecker.LATEST_RELEASE)
                .build()
        );
    }
}
