package io.github.Alathra.Maquillage.listener;

import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        NameColorHandler.removePlayerColor(e.getPlayer());
    }

}
