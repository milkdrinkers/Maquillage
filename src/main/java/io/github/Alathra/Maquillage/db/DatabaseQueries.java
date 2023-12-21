package io.github.Alathra.Maquillage.db;

import io.github.Alathra.Maquillage.utility.DB;
import io.github.Alathra.Maquillage.utility.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static io.github.Alathra.Maquillage.db.schema.Tables.TAGS;
import static io.github.Alathra.Maquillage.db.schema.Tables.TAGS_PLAYERS;
import static io.github.Alathra.Maquillage.db.schema.Tables.COLORS;
import static io.github.Alathra.Maquillage.db.schema.Tables.COLORS_PLAYERS;

/**
 * A holder class for all SQL queries
 */
public abstract class DatabaseQueries {

    // Saves a new tag.
    public static void saveTag(String tag, String perm) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .insertInto(TAGS)
                .set(TAGS.TAG, tag)
                .set(TAGS.PERM, perm)
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    // Saves a new color.
    public static void saveColor(String color, String perm) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .insertInto(COLORS)
                .set(COLORS.COLOR, color)
                .set(COLORS.PERM, perm)
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    // Saves a player with their associated tag. Note that "tag" is the id of the tag.
    public static void savePlayerTag(UUID uuid, int tag) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .insertInto(TAGS_PLAYERS)
                .set(TAGS_PLAYERS.PLAYER, convertUUIDToBytes(uuid))
                .set(TAGS_PLAYERS.TAG, tag)
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    // Saves a player with their associated color. Note that "color" is the id of the color.
    public static void savePlayerColor(UUID uuid, int color) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .insertInto(COLORS_PLAYERS)
                .set(COLORS_PLAYERS.PLAYER, convertUUIDToBytes(uuid))
                .set(COLORS_PLAYERS.COLOR, color)
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    // Loads all tags. Should be called on server start and reload.
    public static @Nullable Result<Record3<@NotNull Integer, @NotNull String, @Nullable String>> loadAllTags() {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(TAGS.ID, TAGS.TAG, TAGS.PERM)
                .from(TAGS)
                .fetch();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
        return null;
    }

    // Loads all colors. Should be called on server start and reload.
    public static @Nullable Result<Record3<@NotNull Integer, @NotNull String, @Nullable String>> loadAllColors() {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(COLORS.ID, COLORS.COLOR, COLORS.PERM)
                .from(COLORS)
                .fetch();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
        return null;
    }

    // Loads a player's tag. Called on player join
    public static @Nullable Record1<Integer> loadPlayerTag(byte[] uuid) {
        try (
            Connection con = DB.getConnection();
            ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(TAGS_PLAYERS.TAG)
                .where(TAGS_PLAYERS.PLAYER.equal(uuid))
                .fetchOne();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
        return null;
    }

    public static @Nullable Record1<Integer> loadPlayerTag(UUID uuid) {
        return loadPlayerTag(convertUUIDToBytes(uuid));
    }

    public static @Nullable Record1<Integer> loadPlayertag(Player p) {
        return loadPlayerTag(p.getUniqueId());
    }

    // Loads a player's tag. Called on player join
    public static @Nullable Record1<Integer> loadPlayerColor(byte[] uuid) {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(COLORS_PLAYERS.COLOR)
                .where(COLORS_PLAYERS.PLAYER.equal(uuid))
                .fetchOne();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
        return null;
    }

    public static @Nullable Record1<Integer> loadPlayerColor(UUID uuid) {
        return loadPlayerColor(convertUUIDToBytes(uuid));
    }

    public static @Nullable Record1<Integer> loadPlayerColor(Player p) {
        return loadPlayerColor(p.getUniqueId());
    }

    public static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID convertBytesToUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }
}
