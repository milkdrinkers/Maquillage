package io.github.alathra.maquillage.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerDataBuilder {
    private @Nullable UUID uuid;
    private @Nullable Player player;
    private int nameColorId = -1;
    private int tagId = -1;
    private String nickname = null;

    public PlayerDataBuilder withUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public PlayerDataBuilder withPlayer(Player player) {
        this.player = player;
        return this;
    }

    public PlayerDataBuilder withNameColorId(int nameColorId) {
        this.nameColorId = nameColorId;
        return this;
    }

    public PlayerDataBuilder withTagId(int tagId) {
        this.tagId = tagId;
        return this;
    }

    public PlayerDataBuilder withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public PlayerData build() {
        if (uuid == null)
            throw new IllegalStateException("Missing state uuid when creating PlayerData object");

        if (player == null)
            throw new IllegalStateException("Missing state player when creating PlayerData object");

        return new PlayerData(
            uuid,
            player,
            nameColorId,
            tagId,
            nickname
        );
    }
}