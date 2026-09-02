package io.github.milkdrinkers.maquillage.api.event.nickname;

import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Event triggered after a player's nickname has been removed.
 *
 * @see PlayerNicknamePreRemoveEvent
 * @see PlayerNicknameChangeEvent
 * @see PlayerNicknamePreChangeEvent
 */
public class PlayerNicknameRemoveEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final @NotNull OfflinePlayer player;
    private final @Nullable Nickname previousNickname;

    public PlayerNicknameRemoveEvent(@NotNull OfflinePlayer player, @Nullable Nickname previousNickname) {
        Objects.requireNonNull(player, "Player cannot be set to null in PlayerNicknameRemoveEvent");
        this.player = player;
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
