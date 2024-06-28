package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.events.PlayerDataLoadedEvent;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.Tag;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jooq.Record1;

import java.util.Hashtable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent e) {
//        if (!e.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED))
//            return;
//
//        UUID uuid = e.getUniqueId();
//        NameColorHandler.loadPlayerColor(uuid);
//        TagHandler.loadPlayerTag(uuid);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        loadPlayerData(p).thenAccept(ids -> {
            Bukkit.getScheduler().runTask(Maquillage.getInstance(), () -> {
                if (ids[0] != -1)
                    NameColorHandler.putPlayerColor(p, ids[0]);
                if (ids[1] != -1)
                    TagHandler.putPlayerTag(p, ids[1]);

                PlayerDataLoadedEvent firedEvent = new PlayerDataLoadedEvent(p, ids[0], ids[1]);
                Bukkit.getPluginManager().callEvent(firedEvent);


//                if (TagHandler.doesPlayerHaveTag(p) && NameColorHandler.doesPlayerHaveColor(p))
//                    UpdateDisplayName.updateDisplayName(p, TagHandler.getPlayerTag(p), NameColorHandler.getPlayerColor(p));
//                else if (TagHandler.doesPlayerHaveTag(p) && !NameColorHandler.doesPlayerHaveColor(p))
//                    UpdateDisplayName.updateDisplayNameNoColor(p, TagHandler.getPlayerTag(p));
//                else if (!TagHandler.doesPlayerHaveTag(p) && NameColorHandler.doesPlayerHaveColor(p))
//                    UpdateDisplayName.updateDisplayNameNoTag(p, NameColorHandler.getPlayerColor(p));
//                else if (!TagHandler.doesPlayerHaveTag(p) && !NameColorHandler.doesPlayerHaveColor(p))
//                    return;
            });
        });
    }

    private CompletableFuture<int[]> loadPlayerData(Player p) {
        return CompletableFuture.supplyAsync(() -> {
            Record1<Integer> colorRecord = DatabaseQueries.loadPlayerColor(p);
            int colorID;
            if (colorRecord == null)
                colorID = -1;
            else
                colorID = colorRecord.component1();

            Record1<Integer> tagRecord = DatabaseQueries.loadPlayerTag(p);
            int tagID;
            if (tagRecord == null)
                tagID = -1;
            else
                tagID = tagRecord.component1();

            return new int[]{colorID, tagID};
        });
    }

}
