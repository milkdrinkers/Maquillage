package io.github.milkdrinkers.maquillage.api.event.nickname;

import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Event triggered before a player's nickname is changed.
 *
 * @implSpec This event allows for modifying the nickname before it is set and for cancellation of the nickname change.
 * @implNote This event is not called when a player's nickname is removed.
 * @see PlayerNicknameChangeEvent
 * @see PlayerNicknameRemoveEvent
 * @see PlayerNicknamePreRemoveEvent
 */
public class PlayerNicknamePreChangeEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;

    private final @NotNull OfflinePlayer player;
    private @NotNull Nickname nickname;
    private final @Nullable Nickname previousNickname;

    public PlayerNicknamePreChangeEvent(@NotNull OfflinePlayer player, @NotNull Nickname nickname, @Nullable Nickname previousNickname) {
        Objects.requireNonNull(player, "Player cannot be set to null in PlayerNicknamePreChangeEvent");
        Objects.requireNonNull(nickname, "Nickname cannot be set to null in PlayerNicknamePreChangeEvent");
        this.player = player;
        this.nickname = nickname;
        this.previousNickname = previousNickname;
    }

    /**
     * Get the player whose nickname is being changed.
     *
     * @return the player
     */
    public @NotNull OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * Get the new nickname of the player.
     *
     * @return the new nickname
     */
    public @NotNull Nickname getNickname() {
        return nickname;
    }

    /**
     * Set the new nickname of the player.
     *
     * @param nickname the new nickname to set
     */
    public void setNickname(@NotNull Nickname nickname) {
        Objects.requireNonNull(nickname, "Nickname cannot be set to null in PlayerNicknamePreChangeEvent");
        this.nickname = nickname;
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
