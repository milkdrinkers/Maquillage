package io.github.alathra.maquillage.listener.listeners;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.database.DatabaseQueries;
import io.github.alathra.maquillage.event.PlayerDataLoadedEvent;
import io.github.alathra.maquillage.player.PlayerData;
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

        loadPlayerData(p).thenAccept(data -> {
            Bukkit.getScheduler().runTask(Maquillage.getInstance(), () -> {
                // Add player to cacheAdd
                PlayerDataHolder.getInstance().setPlayerData(p, data);

                PlayerDataLoadedEvent firedEvent = new PlayerDataLoadedEvent(p, data);
                Bukkit.getPluginManager().callEvent(firedEvent);
            });
        });
    }

    private CompletableFuture<PlayerData> loadPlayerData(Player p) {
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

            Record1<String> nicknameRecord = DatabaseQueries.loadPlayerNickname(p);
            String nick;
            if (nicknameRecord == null)
                nick = null;
            else
                nick = nicknameRecord.component1();

            return new PlayerDataBuilder()
                .withUuid(p.getUniqueId())
                .withPlayer(p)
                .withNameColorId(colorID)
                .withTagId(tagID)
                .withNickname(nick)
                .build();
        });
    }

}
