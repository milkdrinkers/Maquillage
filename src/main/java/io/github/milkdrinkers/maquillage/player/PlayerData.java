package io.github.milkdrinkers.maquillage.player;

import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Holds all Maquillage data for a player
 */
public class PlayerData {
    // Data
    private final UUID uuid;
    private final Player player;

    // Maquillage Data
    private @Nullable NameColor nameColor;
    private @Nullable Tag tag;
    private @Nullable Nickname nickname;
    private int nameColorId;
    private int tagId;

    PlayerData(
        UUID uuid,
        Player player,
        int nameColor,
        int tag,
        @Nullable Nickname nickname
    ) {
        this.uuid = uuid;
        this.player = player;
        this.nameColor = NameColorHolder.getInstance().getByDatabaseId(nameColor);
        this.tag = TagHolder.getInstance().getByDatabaseId(tag);
        this.nickname = nickname;
        this.nameColorId = nameColor;
        this.tagId = tag;
    }

    // SECTION Getters

    /**
     * Get the associated players UUID
     *
     * @return player UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the associated player
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the associated players name color
     *
     * @return name color
     */
    public Optional<NameColor> getNameColor() {
        return Optional.ofNullable(nameColor);
    }

    /**
     * Get the associated players tag
     *
     * @return tag
     */
    public Optional<Tag> getTag() {
        return Optional.ofNullable(tag);
    }

    public Optional<Nickname> getNickname() {
        return Optional.ofNullable(nickname);
    }

    public int getNameColorId() {
        return nameColorId;

    }

    public int getTagId() {
        return tagId;
    }

    public String getNicknameString() {
        return getNickname().map(Nickname::getNickname).orElse("");
    }

    // SECTION Setters

    /**
     * Set the associated players name color
     *
     * @param nameColor A name color or null for nothing
     */
    public void setNameColor(@Nullable NameColor nameColor) {
        if (nameColor == null)
            setNameColor(-1);
        else
            setNameColor(nameColor.getDatabaseId());
    }

    /**
     * Set the associated players name color
     *
     * @param nameColorId A name color id or -1 for nothing
     */
    public void setNameColor(int nameColorId) {
        this.nameColor = NameColorHolder.getInstance().getByDatabaseId(nameColorId);
        this.nameColorId = nameColorId;
    }

    /**
     * Set the associated players name color to nothing
     * <p>
     * Shorthand for calling {@link PlayerData#setNameColor(NameColor)} with {@code  null} as the parameter.
     */
    public void clearNameColor() {
        setNameColor(null);
    }

    /**
     * Set the associated players tag
     *
     * @param tag A tag or null for nothing
     */
    public void setTag(@Nullable Tag tag) {
        if (tag == null)
            setTag(-1);
        else
            setTag(tag.getDatabaseId());
    }

    /**
     * Set the associated players tag
     *
     * @param tagId A tag id or -1 for nothing
     */
    public void setTag(int tagId) {
        this.tag = TagHolder.getInstance().getByDatabaseId(tagId);
        this.tagId = tagId;
    }

    /**
     * Set the associated players tag to nothing
     * <p>
     * Shorthand for calling {@link PlayerData#setTag(Tag)} with {@code  null} as the parameter.
     */
    public void clearTag() {
        setTag(null);
    }


    /**
     * Set the associated players nickname
     *
     * @param nickname The desired nickname
     */
    public void setNickname(@Nullable Nickname nickname) {
        this.nickname = nickname;
    }

    /**
     * Set the associated players nickname to nothing
     */
    public void clearNickname() {
        setNickname(null);
    }

    // SECTION Misc

    /**
     * Compare two PlayerData objects
     *
     * @param playerData player data
     * @return true when player UUID is the same
     */
    public boolean equals(PlayerData playerData) {
        return getUuid() == playerData.getUuid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerData playerData)) return false;
        return equals(playerData);
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }

    @Override
    public String toString() {
        return "PlayerData{" +
            "uuid=" + uuid +
            ", player=" + player +
            ", nameColor=" + nameColor +
            ", tag=" + tag +
            ", nickname=" + nickname +
            '}';
    }
}
