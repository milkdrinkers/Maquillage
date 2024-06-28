package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
        if (!e.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
            return;
        
        UUID uuid = e.getUniqueId();
        NameColorHandler.loadPlayerColor(uuid);
        TagHandler.loadPlayerTag(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (TagHandler.doesPlayerHaveTag(p) && NameColorHandler.doesPlayerHaveColor(p))
            UpdateDisplayName.updateDisplayName(p, TagHandler.getPlayerTag(p), NameColorHandler.getPlayerColor(p));
        else if (TagHandler.doesPlayerHaveTag(p) && !NameColorHandler.doesPlayerHaveColor(p))
            UpdateDisplayName.updateDisplayNameNoColor(p, TagHandler.getPlayerTag(p));
        else if (!TagHandler.doesPlayerHaveTag(p) && NameColorHandler.doesPlayerHaveColor(p))
            UpdateDisplayName.updateDisplayNameNoTag(p, NameColorHandler.getPlayerColor(p));
        else if (!TagHandler.doesPlayerHaveTag(p) && !NameColorHandler.doesPlayerHaveColor(p))
            return;
    }

}
