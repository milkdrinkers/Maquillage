package io.github.Alathra.Maquillage.listener;

import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoinListener(PlayerJoinEvent e) {
        NameColorHandler.loadPlayerColor(e.getPlayer());
    }

}
