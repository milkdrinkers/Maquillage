package io.github.milkdrinkers.maquillage.listener.listeners;

import io.github.milkdrinkers.maquillage.cooldown.Cooldowns;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.event.PlayerDataLoadedEvent;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataBuilder;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.threadutil.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        final Player p = e.getPlayer();

        Scheduler
            .async(() -> {
                Queries.Cooldown.load(e.getPlayer()).forEach((cooldownType, instant) -> {
                    Cooldowns.set(e.getPlayer(), cooldownType, instant);
                });
                return loadPlayerData(p);
            })
            .sync((data) -> {
                // Add player to cacheAdd
                PlayerDataHolder.getInstance().setPlayerData(p, data);

                PlayerDataLoadedEvent firedEvent = new PlayerDataLoadedEvent(p, data);
                Bukkit.getPluginManager().callEvent(firedEvent);
            })
            .execute();
    }

    private PlayerData loadPlayerData(Player p) {
        final Optional<Integer> namecolorId = Queries.NameColor.Players.loadPlayerColor(p);
        final Optional<Integer> tagId = Queries.Tag.Players.loadPlayerTag(p);
        final Optional<Nickname> nickname = Queries.Nickname.loadPlayerNickname(p);

        return new PlayerDataBuilder()
            .withUuid(p.getUniqueId())
            .withPlayer(p)
            .withNameColorId(namecolorId.orElse(-1))
            .withTagId(tagId.orElse(-1))
            .withNickname(nickname.orElse(null))
            .build();
    }
}
