package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.events.PlayerDataLoadedEvent;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.Logger;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataLoadedListener implements Listener {

    @EventHandler
    public static void onPlayerDataLoaded(PlayerDataLoadedEvent e) {
        Player p = e.getPlayer();

        if (TagHandler.doesPlayerHaveTag(p) && NameColorHandler.doesPlayerHaveColor(p))
            UpdateDisplayName.updateDisplayName(p, TagHandler.getPlayerTag(p), NameColorHandler.getPlayerColor(p));
        else if (TagHandler.doesPlayerHaveTag(p) && !NameColorHandler.doesPlayerHaveColor(p))
            UpdateDisplayName.updateDisplayNameNoColor(p, TagHandler.getPlayerTag(p));
        else if (!TagHandler.doesPlayerHaveTag(p) && NameColorHandler.doesPlayerHaveColor(p))
            UpdateDisplayName.updateDisplayNameNoTag(p, NameColorHandler.getPlayerColor(p));
        else if (!TagHandler.doesPlayerHaveTag(p) && !NameColorHandler.doesPlayerHaveColor(p))
            return;

        Logger.get().info("Caught DataLoadedEvent and updated displayname");
    }
}
