package io.github.milkdrinkers.maquillage.event;

import io.github.milkdrinkers.maquillage.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDataLoadedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private PlayerData data;

    public PlayerDataLoadedEvent(Player player, PlayerData data) {
        this.player = player;
        this.data = data;
    }

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerData getData() {
        return data;
    }
}
