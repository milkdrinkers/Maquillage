package io.github.milkdrinkers.maquillage.listener.listeners;

import io.github.milkdrinkers.maquillage.cooldown.Cooldown;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.threadutil.Scheduler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        PlayerDataHolder.getInstance().removePlayerData(e.getPlayer());
        Scheduler
            .async(() -> {
                Queries.Cooldown.save(e.getPlayer());
                Cooldown.getInstance().clearCooldowns(e.getPlayer());
            })
            .execute();
    }
}
