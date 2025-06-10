package io.github.milkdrinkers.maquillage.api.event.nickname;

import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Event triggered before a player's nickname is removed.
 *
 * @implSpec This event allows for cancellation of the nickname removal.
 * @see PlayerNicknameRemoveEvent
 * @see PlayerNicknameChangeEvent
 * @see PlayerNicknamePreChangeEvent
 */
public class PlayerNicknamePreRemoveEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;

    private final @NotNull Player player;
    private final @Nullable Nickname previousNickname;

    public PlayerNicknamePreRemoveEvent(@NotNull Player player, @Nullable Nickname previousNickname) {
        Objects.requireNonNull(player, "Player cannot be set to null in PlayerNicknamePreRemoveEvent");
        this.player = player;
        this.previousNickname = previousNickname;
    }

    /**
     * Get the player whose nickname is being changed.
     *
     * @return the player
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * Get the previous nickname of the player, if any.
     *
     * @return the previous nickname, or null if there was no previous nickname
     */
    public @Nullable Nickname getPreviousNickname() {
        return previousNickname;
    }

    /**
     * Get the previous nickname of the player, if any.
     *
     * @return an Optional containing the previous nickname, or empty if there was no previous nickname
     */
    private Optional<Nickname> getPreviousNicknameOptional() {
        return Optional.ofNullable(previousNickname);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
