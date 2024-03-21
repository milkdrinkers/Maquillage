package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(AsyncPlayerPreLoginEvent e) {
        if (!e.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        
        UUID uuid = e.getUniqueId();
        NameColorHandler.loadPlayerColor(uuid);
        TagHandler.loadPlayerTag(uuid);
    }

}
