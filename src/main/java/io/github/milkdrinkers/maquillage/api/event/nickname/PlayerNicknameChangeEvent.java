package io.github.milkdrinkers.maquillage.api.event.nickname;

import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Event triggered after a player's nickname has changed.
 *
 * @implNote This event is not called when a player's nickname is removed.
 * @see PlayerNicknamePreChangeEvent
 * @see PlayerNicknameRemoveEvent
 * @see PlayerNicknamePreRemoveEvent
 */
public class PlayerNicknameChangeEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @NotNull Player player;
    private final @NotNull Nickname nickname;
    private final @Nullable Nickname previousNickname;

    public PlayerNicknameChangeEvent(@NotNull Player player, @NotNull Nickname nickname, @Nullable Nickname previousNickname) {
        Objects.requireNonNull(player, "Player cannot be set to null in PlayerNicknameChangeEvent");
        Objects.requireNonNull(nickname, "Nickname cannot be set to null in PlayerNicknameChangeEvent");
        this.player = player;
        this.nickname = nickname;
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
     * Get the new nickname of the player.
     *
     * @return the new nickname
     */
    public @NotNull Nickname getNickname() {
        return nickname;
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

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
