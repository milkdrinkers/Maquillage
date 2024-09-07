package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.player.PlayerDataHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        PlayerDataHolder.getInstance().removePlayerData(e.getPlayer());
    }
}
