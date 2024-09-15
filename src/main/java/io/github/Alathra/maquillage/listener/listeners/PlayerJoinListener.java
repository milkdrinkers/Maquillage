package io.github.alathra.maquillage.listener.listeners;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.database.DatabaseQueries;
import io.github.alathra.maquillage.events.PlayerDataLoadedEvent;
import io.github.alathra.maquillage.player.PlayerDataBuilder;
import io.github.alathra.maquillage.player.PlayerDataHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jooq.Record1;

import java.util.concurrent.CompletableFuture;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        loadPlayerData(p).thenAccept(ids -> {
            Bukkit.getScheduler().runTask(Maquillage.getInstance(), () -> {
                // Add player to cacheAdd
                PlayerDataHolder.getInstance().setPlayerData(
                    p,
                    new PlayerDataBuilder()
                        .withUuid(p.getUniqueId())
                        .withPlayer(p)
                        .withNameColorId(ids[0])
                        .withTagId(ids[1])
                        .build()
                );

                PlayerDataLoadedEvent firedEvent = new PlayerDataLoadedEvent(p, ids[0], ids[1]);
                Bukkit.getPluginManager().callEvent(firedEvent);


//                if (TagHolder.doesPlayerHaveTag(p) && NameColorHolder.doesPlayerHaveColor(p))
//                    UpdateDisplayName.updateDisplayName(p, TagHolder.getPlayerTag(p), NameColorHolder.getPlayerColor(p));
//                else if (TagHolder.doesPlayerHaveTag(p) && !NameColorHolder.doesPlayerHaveColor(p))
//                    UpdateDisplayName.updateDisplayNameNoColor(p, TagHolder.getPlayerTag(p));
//                else if (!TagHolder.doesPlayerHaveTag(p) && NameColorHolder.doesPlayerHaveColor(p))
//                    UpdateDisplayName.updateDisplayNameNoTag(p, NameColorHolder.getPlayerColor(p));
//                else if (!TagHolder.doesPlayerHaveTag(p) && !NameColorHolder.doesPlayerHaveColor(p))
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
