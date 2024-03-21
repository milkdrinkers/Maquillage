package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> {
            Player p = e.getPlayer();
            NameColorHandler.removePlayerColor(p);
            TagHandler.removePlayerTag(p);
        });
    }

}
