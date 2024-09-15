package io.github.alathra.maquillage.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDataLoadedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private int colorID;
    private int tagID;

    public PlayerDataLoadedEvent(Player player, int colorID, int tagID) {
        this.player = player;
        this.colorID = colorID;
        this.tagID = tagID;
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

    public int getColorID() {
        return colorID;
    }

    public int getTagID() {
        return tagID;
    }

}
