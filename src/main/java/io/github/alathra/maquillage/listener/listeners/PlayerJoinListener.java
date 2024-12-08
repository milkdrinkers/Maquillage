package io.github.alathra.maquillage.listener.listeners;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.database.Queries;
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

import java.util.Optional;
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
            final Optional<Integer> namecolorId = Queries.NameColor.Players.loadPlayerColor(p);
            final Optional<Integer> tagId = Queries.Tag.Players.loadPlayerTag(p);
            final Optional<String> nickname = Queries.Nickname.loadPlayerNickname(p);

            return new PlayerDataBuilder()
                .withUuid(p.getUniqueId())
                .withPlayer(p)
                .withNameColorId(namecolorId.orElse(-1))
                .withTagId(tagId.orElse(-1))
                .withNickname(nickname.orElse(null))
                .build();
        });
    }

}
