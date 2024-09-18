package io.github.alathra.maquillage.database;

import io.github.alathra.maquillage.utility.DB;
import io.github.alathra.maquillage.utility.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.Record;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static io.github.alathra.maquillage.database.schema.Tables.*;

/**
 * A holder class for all SQL queries
 */
public abstract class DatabaseQueries {
    /**
     * Saves a new tag.
     *
     * @param tag the tag
     * @return the id of the tag or -1 if saving failed
     */
    public static int saveTag(String tag, String perm, String label) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            Record1<Integer> record = context
                .insertInto(TAGS, TAGS.TAG, TAGS.PERM, TAGS.LABEL)
                .values(
                    tag,
                    perm,
                    label
                )
                .returningResult(TAGS.ID)
                .fetchOne();

            if (record == null)
                throw new SQLException("Failed to save new tag. The returned tag id was null!");

            return record.component1();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
            return -1;
        }
    }

    public static boolean updateTag(String tag, String perm, String label, int databaseId) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context.update(TAGS)
                .set(TAGS.TAG, tag)
                .set(TAGS.PERM, perm)
                .set(TAGS.LABEL, label)
                .where(TAGS.ID.eq(databaseId))
                .execute();
            return true;

        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
            return false;
        }
    }

    public static boolean removeTag(int databaseId) {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            context.deleteFrom(TAGS)
                .where(TAGS.ID.eq(databaseId))
                .execute();
            return true;

        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
            return false;
        }
    }

    /**
     * Saves a new color.
     *
     * @param color the color
     * @return the id of the color or -1 if saving failed
     */
    public static int saveColor(String color, String perm, String label) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            Record1<Integer> record = context
                .insertInto(COLORS, COLORS.COLOR, COLORS.PERM, COLORS.LABEL)
                .values(
                    color,
                    perm,
                    label
                )
                .returningResult(COLORS.ID)
                .fetchOne();

            if (record == null)
                throw new SQLException("Failed to save new color. The returned color id was null!");

            return record.component1();

        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
            return -1;
        }
    }

    public static boolean updateColor(String color, String perm, String label, int databaseId) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context.update(COLORS)
                .set(COLORS.COLOR, color)
                .set(COLORS.PERM, perm)
                .set(COLORS.LABEL, label)
                .where(COLORS.ID.eq(databaseId))
                .execute();
            return true;

        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
            return false;
        }
    }

    public static boolean removeColor(int databaseId) {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            context.deleteFrom(COLORS)
                .where(COLORS.ID.eq(databaseId))
                .execute();
            return true;

        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
            return false;
        }
    }

    // Saves a player with their associated tag. Note that "tag" is the id of the tag.
    public static void savePlayerTag(UUID uuid, int tag) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .insertInto(TAGS_PLAYERS, TAGS_PLAYERS.PLAYER, TAGS_PLAYERS.TAG)
                .values(
                    convertUUIDToBytes(uuid),
                    tag
                )
                .onDuplicateKeyUpdate()
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
                .insertInto(COLORS_PLAYERS, COLORS_PLAYERS.PLAYER, COLORS_PLAYERS.COLOR)
                .values(
                    convertUUIDToBytes(uuid),
                    color
                )
                .onDuplicateKeyUpdate()
                .set(COLORS_PLAYERS.PLAYER, convertUUIDToBytes(uuid))
                .set(COLORS_PLAYERS.COLOR, color)
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    /**
     * Deletes entry for a player's tag.
     *
     * @param uuid
     */
    public static void removePlayerTag(UUID uuid) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .delete(TAGS_PLAYERS)
                .where(TAGS_PLAYERS.PLAYER.equal(convertUUIDToBytes(uuid)))
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    /**
     * Deletes entry for a player's color.
     *
     * @param uuid
     */
    public static void removePlayerColor(UUID uuid) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .delete(COLORS_PLAYERS)
                .where(COLORS_PLAYERS.PLAYER.equal(convertUUIDToBytes(uuid)))
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
    }

    public static @Nullable org.jooq.Record loadColor(int databaseId) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(COLORS.fields(COLORS.COLOR, COLORS.PERM, COLORS.LABEL))
                .from(COLORS)
                .where(COLORS.ID.equal(databaseId))
                .fetchOne();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!" + e);
        }
        return null;
    }

    public static @Nullable Record loadTag(final int databaseId) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(TAGS.fields(TAGS.TAG, TAGS.PERM, TAGS.LABEL))
                .from(TAGS)
                .where(TAGS.ID.equal(databaseId))
                .fetchOne();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!" + e);
        }
        return null;
    }

    public static void saveSyncMessage(final String message) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context.insertInto(SYNC, SYNC.MESSAGE, SYNC.TIMESTAMP)
                .values(
                    message,
                    LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
                )
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!" + e);
        }
    }

    public static @Nullable Result<Record3<Integer, String, LocalDateTime>> fetchSyncMessages(final int latestSyncId) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            return context.select(SYNC.ID, SYNC.MESSAGE, SYNC.TIMESTAMP)
                .from(SYNC)
                .where(SYNC.ID.greaterThan(latestSyncId))
                .fetch();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!" + e);
        }
        return null;
    }

    public static void cleanUpSyncMessages() {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context.deleteFrom(SYNC)
                .where(SYNC.TIMESTAMP.lessOrEqual(
                    LocalDateTime.ofInstant(
                        Instant.now().minus(120, ChronoUnit.SECONDS),
                        ZoneOffset.UTC)))
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!" + e);
        }
    }

    // Loads all tags. Should be called on server start and reload.
    public static @Nullable Result<Record4<@NotNull Integer, @NotNull String, @Nullable String, @NotNull String>> loadAllTags() {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(TAGS.ID, TAGS.TAG, TAGS.PERM, TAGS.LABEL)
                .from(TAGS)
                .fetch();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
        return null;
    }

    // Loads all colors. Should be called on server start and reload.
    public static @Nullable Result<Record4<@NotNull Integer, @NotNull String, @Nullable String, @NotNull String>> loadAllColors() {
        try (
            Connection con = DB.getConnection();
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(COLORS.ID, COLORS.COLOR, COLORS.PERM, COLORS.LABEL)
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
                .from(TAGS_PLAYERS)
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

    public static @Nullable Record1<Integer> loadPlayerTag(Player p) {
        return loadPlayerTag(p.getUniqueId());
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
                .from(COLORS_PLAYERS)
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

    /**
     * Saves a player's nickname.
     *
     * @param uuid a byte array representing the player's UUID.
     * @param nickname the player's nickname.
     */
    public static void savePlayerNickname(byte[] uuid, String nickname) {
        try (
            Connection  con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .insertInto(NICKNAMES, NICKNAMES.PLAYER, NICKNAMES.NICKNAME)
                .values(
                    uuid,
                    nickname
                )
                .onDuplicateKeyUpdate()
                .set(NICKNAMES.PLAYER, uuid)
                .set(NICKNAMES.NICKNAME, nickname)
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!" + e);
        }
    }

    /**
     * Convenience method for {@link DatabaseQueries#savePlayerNickname(byte[], String)}
     *
     * @param uuid
     * @param nickname
     */
    public static void savePlayerNickname(UUID uuid, String nickname) {
        savePlayerNickname(convertUUIDToBytes(uuid), nickname);
    }

    /**
     * Convenience method for {@link DatabaseQueries#savePlayerNickname(byte[], String)}
     *
     * @param p
     * @param nickname
     */
    public static void savePlayerNickname(Player p, String nickname) {
        savePlayerNickname(p.getUniqueId(), nickname);
    }

    /**
     * Loads a player's nickname.
     *
     * @param uuid byte array representing the player's UUID.
     * @return the player's nickname, in a jOOQ record.
     */
    public static @Nullable Record1<String> loadPlayerNickname(byte[] uuid) {
        try(
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            return context
                .select(NICKNAMES.NICKNAME)
                .from(NICKNAMES)
                .where(NICKNAMES.PLAYER.equal(uuid))
                .fetchOne();
        } catch (SQLException e) {
            Logger.get().error("SQL Query threw an error!", e);
        }
        return null;
    }

    /**
     * Convenience method for {@link DatabaseQueries#loadPlayerNickname(byte[])}
     *
     * @param uuid the player's UUID.
     * @return the player's nickname, in a jOOQ record.
     */
    public static @Nullable Record1<String> loadPlayerNickname(UUID uuid) {
        return loadPlayerNickname(convertUUIDToBytes(uuid));
    }

    /**
     * Convenience method for {@link DatabaseQueries#loadPlayerNickname(byte[])}
     *
     * @param p the player object.
     * @return the player's nickname, in a jOOQ record.
     */
    public static @Nullable Record1<String> loadPlayerNickname(Player p) {
        return loadPlayerNickname(p.getUniqueId());
    }

    /**
     * Clears a player's nickname.
     *
     * @param uuid a byte array representing the player's UUID.
     */
    public static void clearPlayerNickname(byte[] uuid) {
        try (
            Connection con = DB.getConnection()
        ) {
            DSLContext context = DB.getContext(con);

            context
                .deleteFrom(NICKNAMES)
                .where(NICKNAMES.PLAYER.equal(uuid))
                .execute();
        } catch (SQLException e) {
            Logger.get().error("SQL Exception caught an error!" + e);
        }
    }

    /**
     * Convenience method for {@link DatabaseQueries#clearPlayerNickname(byte[])}
     *
     * @param uuid
     */
    public static void clearPlayerNickname(UUID uuid) {
        clearPlayerNickname(convertUUIDToBytes(uuid));
    }

    /**
     * Convenience method for {@link DatabaseQueries#clearPlayerNickname(byte[])}
     *
     * @param p
     */
    public static void clearPlayerNickname(Player p) {
        clearPlayerNickname(convertUUIDToBytes(p.getUniqueId()));
    }

    /**
     * Convert uuid to an array of bytes.
     *
     * @param uuid the uuid
     * @return the byte array
     */
    public static byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /**
     * Convert byte array to uuid.
     *
     * @param bytes the byte array
     * @return the uuid
     */
    public static UUID convertBytesToUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        return new UUID(high, low);
    }
}
