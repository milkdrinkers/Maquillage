package io.github.Alathra.Maquillage.listener.listeners;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        NameColorHandler.loadPlayerColor(p);
        if (!NameColorHandler.doesPlayerHaveColor(p))
            return;
        Component name = ColorParser.of( NameColorHandler.getPlayerColorString(p) + p.getName()).build();
        p.playerListName(name);
        p.displayName(name);
    }

}
