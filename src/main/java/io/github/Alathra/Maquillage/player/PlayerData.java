package io.github.Alathra.Maquillage.player;

import io.github.Alathra.Maquillage.module.namecolor.NameColor;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.Tag;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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

    PlayerData(
        UUID uuid,
        Player player,
        int nameColor,
        int tag
    ) {
        this.uuid = uuid;
        this.player = player;
        this.nameColor = NameColorHolder.getInstance().getByID(nameColor);
        this.tag = TagHolder.getInstance().getByID(tag);
    }

    // SECTION Getters

    /**
     * Get the associated players UUID
     * @return player UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the associated player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the associated players name color
     * @return name color
     */
    public Optional<NameColor> getNameColor() {
        return Optional.ofNullable(nameColor);
    }

    /**
     * Get the associated players tag
     * @return tag
     */
    public Optional<Tag> getTag() {
        return Optional.ofNullable(tag);
    }

    // SECTION Setters

    /**
     * Set the associated players name color
     * @param nameColor A name color or null for nothing
     */
    public void setNameColor(@Nullable NameColor nameColor) {
        if (nameColor == null)
            setNameColor(-1);
        else
            setNameColor(nameColor.getID());
    }

    /**
     * Set the associated players name color
     * @param nameColorId A name color id or -1 for nothing
     */
    public void setNameColor(int nameColorId) {
        this.nameColor = NameColorHolder.getInstance().getByID(nameColorId);
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
     * @param tag A tag or null for nothing
     */
    public void setTag(@Nullable Tag tag) {
        if (tag == null)
            setTag(-1);
        else
            setTag(tag.getID());
    }

    /**
     * Set the associated players tag
     * @param tagId A tag id or -1 for nothing
     */
    public void setTag(int tagId) {
        this.tag = TagHolder.getInstance().getByID(tagId);
    }

    /**
     * Set the associated players tag to nothing
     * <p>
     * Shorthand for calling {@link PlayerData#setTag(Tag)} with {@code  null} as the parameter.
     */
    public void clearTag() {
        setTag(null);
    }

    // SECTION Misc

    /**
     * Compare two PlayerData objects
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
    public String toString() {
        return "PlayerData{" +
            "uuid=" + uuid +
            ", player=" + player +
            ", nameColor=" + nameColor +
            ", tag=" + tag +
            '}';
    }
}
