package io.github.milkdrinkers.maquillage.database;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.github.milkdrinkers.maquillage.database.schema.tables.records.CooldownsRecord;
import io.github.milkdrinkers.maquillage.database.schema.tables.records.NicknamesRecord;
import io.github.milkdrinkers.maquillage.cooldown.CooldownType;
import io.github.milkdrinkers.maquillage.cooldown.Cooldowns;
import io.github.milkdrinkers.maquillage.database.handler.DatabaseType;
import io.github.milkdrinkers.maquillage.messaging.message.BidirectionalMessage;
import io.github.milkdrinkers.maquillage.messaging.message.IncomingMessage;
import io.github.milkdrinkers.maquillage.messaging.message.OutgoingMessage;
import io.github.milkdrinkers.maquillage.utility.DB;
import io.github.milkdrinkers.maquillage.utility.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.Record;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.milkdrinkers.maquillage.database.schema.tables.Colors.COLORS;
import static io.github.milkdrinkers.maquillage.database.schema.tables.ColorsPlayers.COLORS_PLAYERS;
import static io.github.milkdrinkers.maquillage.database.schema.tables.Cooldowns.COOLDOWNS;
import static io.github.milkdrinkers.maquillage.database.schema.tables.Messaging.MESSAGING;
import static io.github.milkdrinkers.maquillage.database.schema.tables.Nicknames.NICKNAMES;
import static io.github.milkdrinkers.maquillage.database.schema.tables.Tags.TAGS;
import static io.github.milkdrinkers.maquillage.database.schema.tables.TagsPlayers.TAGS_PLAYERS;
import static io.github.milkdrinkers.maquillage.database.QueryUtils.UUIDUtil;
import static org.jooq.impl.DSL.*;

/**
 * A class providing access to all SQL queries.
 */
@SuppressWarnings({"LoggingSimilarMessage", "StringConcatenationArgumentToLogCall"})
@ApiStatus.Internal
public final class Queries {
    /**
     * Holds all queries related to tags
     */
    @ApiStatus.Internal
    public static final class Tag {
        /**
         * Saves a new tag.
         *
         * @param tag   the tag
         * @param perm  the perm
         * @param label the label
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

                if (!DB.getHandler().getDatabaseConfig().getDatabaseType().equals(DatabaseType.SQLITE) && record != null && record.value1() != null) {
                    return record.value1(); // For H2, MySQL, MariaDB
                } else {
                    final int rowId = context.lastID().intValue();
                    context // Update ID field for SQLite
                        .update(TAGS)
                        .set(TAGS.ID, rowId)
                        .where("rowid = ?", rowId)
                        .execute();
                    return rowId; // For SQLite
                }

            } catch (SQLException | ArithmeticException e) {
                Logger.get().error("SQL Query threw an error!", e);
                return -1;
            }
        }

        /**
         * Updates a tag.
         *
         * @param tag   the tag
         * @param perm  the perm
         * @param label the label
         * @param tagId the database id
         * @return the success state of the action
         */
        public static boolean updateTag(String tag, String perm, String label, int tagId) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context.update(TAGS)
                    .set(TAGS.TAG, tag)
                    .set(TAGS.PERM, perm)
                    .set(TAGS.LABEL, label)
                    .where(TAGS.ID.eq(tagId))
                    .execute();
                return true;

            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
                return false;
            }
        }

        /**
         * Remove a tag.
         *
         * @param tagId the database id
         * @return the success state of the action
         */
        public static boolean removeTag(int tagId) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context.deleteFrom(TAGS)
                    .where(TAGS.ID.eq(tagId))
                    .execute();
                return true;

            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
                return false;
            }
        }

        /**
         * Load tag record.
         *
         * @param tagId the database id
         * @return the record
         */
        public static @Nullable Record loadTag(final int tagId) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                return context
                    .select(TAGS.fields(TAGS.TAG, TAGS.PERM, TAGS.LABEL))
                    .from(TAGS)
                    .where(TAGS.ID.equal(tagId))
                    .fetchOne();
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
            }
            return null;
        }

        /**
         * Load all tags from the database.
         *
         * @return the result
         * @implSpec Should be called on server restarts or plugin reloads.
         */
        public static @Nullable Result<Record4<@NotNull Integer, @NotNull String, @Nullable String, @NotNull String>> loadAllTags() {
            try (
                Connection con = DB.getConnection()
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

        /**
         * Holds all queries with player specific behaviour
         */
        @ApiStatus.Internal
        public static final class Players {
            /**
             * Save the selected tag of a player.
             *
             * @param uuid  the player uuid
             * @param tagId the tag id
             */
            public static void savePlayerTag(UUID uuid, int tagId) {
                try (
                    Connection con = DB.getConnection()
                ) {
                    DSLContext context = DB.getContext(con);

                    context
                        .insertInto(TAGS_PLAYERS, TAGS_PLAYERS.PLAYER, TAGS_PLAYERS.TAG)
                        .values(
                            UUIDUtil.toBytes(uuid),
                            tagId
                        )
                        .onDuplicateKeyUpdate()
                        .set(TAGS_PLAYERS.PLAYER, UUIDUtil.toBytes(uuid))
                        .set(TAGS_PLAYERS.TAG, tagId)
                        .execute();
                } catch (SQLException e) {
                    Logger.get().error("SQL Query threw an error!", e);
                }
            }

            /**
             * Delete the selected tag of a player.
             *
             * @param uuid the player uuid
             */
            public static void removePlayerTag(UUID uuid) {
                try (
                    Connection con = DB.getConnection()
                ) {
                    DSLContext context = DB.getContext(con);

                    context
                        .delete(TAGS_PLAYERS)
                        .where(TAGS_PLAYERS.PLAYER.equal(UUIDUtil.toBytes(uuid)))
                        .execute();
                } catch (SQLException e) {
                    Logger.get().error("SQL Query threw an error!", e);
                }
            }

            /**
             * Fetch the selected tag of a player.
             *
             * @param uuid the player uuid
             * @return the tag id, wrapped as optional
             * @implSpec Should only be called on player joins
             */
            public static Optional<Integer> loadPlayerTag(UUID uuid) {
                try (
                    Connection con = DB.getConnection()
                ) {
                    DSLContext context = DB.getContext(con);

                    final Record1<Integer> record = context
                        .select(TAGS_PLAYERS.TAG)
                        .from(TAGS_PLAYERS)
                        .where(TAGS_PLAYERS.PLAYER.equal(UUIDUtil.toBytes(uuid)))
                        .fetchOne();

                    if (record == null)
                        return Optional.empty();

                    return Optional.ofNullable(record.value1());
                } catch (SQLException e) {
                    Logger.get().error("SQL Query threw an error!", e);
                }
                return Optional.empty();
            }

            /**
             * Convenience method for {@link #loadPlayerTag(UUID)}
             *
             * @param p the player
             * @return the tag id, wrapped as optional
             */
            public static Optional<Integer> loadPlayerTag(Player p) {
                return loadPlayerTag(p.getUniqueId());
            }
        }
    }

    /**
     * Holds all queries related to name colors
     */
    @ApiStatus.Internal
    public static final class NameColor {
        /**
         * Saves a new namecolor.
         *
         * @param namecolor the namecolor
         * @param perm      the perm
         * @param label     the label
         * @return the id of the namecolor or -1 if saving failed
         */
        public static int saveColor(String namecolor, String perm, String label) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                Record1<Integer> record = context
                    .insertInto(COLORS, COLORS.COLOR, COLORS.PERM, COLORS.LABEL)
                    .values(
                        namecolor,
                        perm,
                        label
                    )
                    .returningResult(COLORS.ID)
                    .fetchOne();

                if (!DB.getHandler().getDatabaseConfig().getDatabaseType().equals(DatabaseType.SQLITE) && record != null && record.value1() != null) {
                    return record.value1(); // For H2, MySQL, MariaDB
                } else {
                    final int rowId = context.lastID().intValue();
                    context // Update ID field for SQLite
                        .update(COLORS)
                        .set(COLORS.ID, rowId)
                        .where("rowid = ?", rowId)
                        .execute();
                    return rowId; // For SQLite
                }

            } catch (SQLException | ArithmeticException e) {
                Logger.get().error("SQL Query threw an error!", e);
                return -1;
            }
        }

        /**
         * Update a namecolor.
         *
         * @param namecolor   namecolor
         * @param perm        the perm
         * @param label       the label
         * @param namecolorId the database id
         * @return the success state of the action
         */
        public static boolean updateColor(String namecolor, String perm, String label, int namecolorId) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context.update(COLORS)
                    .set(COLORS.COLOR, namecolor)
                    .set(COLORS.PERM, perm)
                    .set(COLORS.LABEL, label)
                    .where(COLORS.ID.eq(namecolorId))
                    .execute();
                return true;

            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
                return false;
            }
        }

        /**
         * Remove a namecolor.
         *
         * @param namecolorId the database id
         * @return the success state of the action
         */
        public static boolean removeColor(int namecolorId) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context.deleteFrom(COLORS)
                    .where(COLORS.ID.eq(namecolorId))
                    .execute();
                return true;

            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
                return false;
            }
        }

        /**
         * Load namecolor record.
         *
         * @param namecolorId the database id
         * @return the record
         */
        public static @Nullable Record loadColor(int namecolorId) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                return context
                    .select(COLORS.fields(COLORS.COLOR, COLORS.PERM, COLORS.LABEL))
                    .from(COLORS)
                    .where(COLORS.ID.equal(namecolorId))
                    .fetchOne();
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
            }
            return null;
        }

        /**
         * Load all namecolors from the database.
         *
         * @return the result
         * @implSpec Should be called on server restarts or plugin reloads.
         */
        public static @Nullable Result<Record4<@NotNull Integer, @NotNull String, @Nullable String, @NotNull String>> loadAllColors() {
            try (
                Connection con = DB.getConnection()
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

        /**
         * Holds all queries with player specific behaviour
         */
        @ApiStatus.Internal
        public static final class Players {
            /**
             * Save the selected namecolor of a player.
             *
             * @param uuid        the player uuid
             * @param namecolorId the namecolor id
             */
            public static void savePlayerColor(UUID uuid, int namecolorId) {
                try (
                    Connection con = DB.getConnection()
                ) {
                    DSLContext context = DB.getContext(con);

                    context
                        .insertInto(COLORS_PLAYERS, COLORS_PLAYERS.PLAYER, COLORS_PLAYERS.COLOR)
                        .values(
                            UUIDUtil.toBytes(uuid),
                            namecolorId
                        )
                        .onDuplicateKeyUpdate()
                        .set(COLORS_PLAYERS.PLAYER, UUIDUtil.toBytes(uuid))
                        .set(COLORS_PLAYERS.COLOR, namecolorId)
                        .execute();
                } catch (SQLException e) {
                    Logger.get().error("SQL Query threw an error!", e);
                }
            }

            /**
             * Deletes the selected namecolor of a player.
             *
             * @param uuid the player uuid
             */
            public static void removePlayerColor(UUID uuid) {
                try (
                    Connection con = DB.getConnection()
                ) {
                    DSLContext context = DB.getContext(con);

                    context
                        .delete(COLORS_PLAYERS)
                        .where(COLORS_PLAYERS.PLAYER.equal(UUIDUtil.toBytes(uuid)))
                        .execute();
                } catch (SQLException e) {
                    Logger.get().error("SQL Query threw an error!", e);
                }
            }

            /**
             * Fetch the selected namecolor of a player.
             *
             * @param uuid the player uuid
             * @return the namecolor id, wrapped as optional
             * @implSpec Should only be called on player joins
             */
            public static Optional<Integer> loadPlayerColor(UUID uuid) {
                try (
                    Connection con = DB.getConnection()
                ) {
                    DSLContext context = DB.getContext(con);

                    final Record1<Integer> record = context
                        .select(COLORS_PLAYERS.COLOR)
                        .from(COLORS_PLAYERS)
                        .where(COLORS_PLAYERS.PLAYER.equal(UUIDUtil.toBytes(uuid)))
                        .fetchOne();

                    if (record == null)
                        return Optional.empty();

                    return Optional.ofNullable(record.value1());
                } catch (SQLException e) {
                    Logger.get().error("SQL Query threw an error!", e);
                }
                return Optional.empty();
            }

            /**
             * Convenience method for {@link #loadPlayerColor(UUID)}
             *
             * @param p the player
             * @return the namecolor id, wrapped as optional
             */
            public static Optional<Integer> loadPlayerColor(Player p) {
                return loadPlayerColor(p.getUniqueId());
            }
        }
    }

    /**
     * Holds all queries related to nicknames
     */
    @ApiStatus.Internal
    public static final class Nickname {
        /**
         * Saves a player's nickname.
         *
         * @param uuid     the player's UUID
         * @param nickname the nickname
         */
        public static void savePlayerNickname(UUID uuid, io.github.milkdrinkers.maquillage.module.nickname.Nickname nickname) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context
                    .insertInto(NICKNAMES, NICKNAMES.PLAYER, NICKNAMES.NICKNAME, NICKNAMES.USERNAME)
                    .values(
                        UUIDUtil.toBytes(uuid),
                        nickname.getNickname(),
                        nickname.getUsername()
                    )
                    .onDuplicateKeyUpdate()
                    .set(NICKNAMES.PLAYER, UUIDUtil.toBytes(uuid))
                    .set(NICKNAMES.NICKNAME, nickname.getNickname())
                    .set(NICKNAMES.USERNAME, nickname.getUsername())
                    .execute();
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
            }
        }

        /**
         * Convenience method for {@link Nickname#savePlayerNickname(UUID, io.github.milkdrinkers.maquillage.module.nickname.Nickname)}
         *
         * @param p        the player
         * @param nickname the nickname
         */
        public static void savePlayerNickname(OfflinePlayer p, io.github.milkdrinkers.maquillage.module.nickname.Nickname nickname) {
            savePlayerNickname(p.getUniqueId(), nickname);
        }

        /**
         * Fetches a player's nickname from the database.
         *
         * @param p the player object.
         * @return the player's nickname, wrapped as optional.
         */
        public static Optional<io.github.milkdrinkers.maquillage.module.nickname.Nickname> loadPlayerNickname(OfflinePlayer p) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                final NicknamesRecord record = context
                    .selectFrom(NICKNAMES)
                    .where(NICKNAMES.PLAYER.equal(UUIDUtil.toBytes(p.getUniqueId())))
                    .fetchOne();

                if (record == null)
                    return Optional.empty();

                // If the player profile is not loaded, we cannot check the name, so we load the player profile.
                final PlayerProfile profile = p.getPlayerProfile();
                if (profile.getName() == null)
                    profile.complete(false); // Load player profile blocking

                final @NotNull String currentUsername = profile.getName();

                // If the joining player has a different name than the one stored in the database, update the stored name.
                if (!currentUsername.equals(record.getUsername())) {
                    context.update(NICKNAMES)
                        .set(NICKNAMES.USERNAME, currentUsername)
                        .where(NICKNAMES.PLAYER.equal(UUIDUtil.toBytes(p.getUniqueId())))
                        .execute();
                }

                return Optional.of(
                    new io.github.milkdrinkers.maquillage.module.nickname.Nickname(
                        record.getNickname(),
                        record.getUsername()
                    )
                );

            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
            }
            return Optional.empty();
        }

        /**
         * Clears a player's nickname in the database.
         *
         * @param uuid the player's UUID.
         */
        public static void clearPlayerNickname(UUID uuid) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context
                    .deleteFrom(NICKNAMES)
                    .where(NICKNAMES.PLAYER.equal(UUIDUtil.toBytes(uuid)))
                    .execute();
            } catch (SQLException e) {
                Logger.get().error("SQL Exception caught an error!" + e);
            }
        }

        /**
         * Convenience method for {@link Nickname#clearPlayerNickname(UUID)}.
         *
         * @param p the player
         */
        public static void clearPlayerNickname(OfflinePlayer p) {
            clearPlayerNickname(p.getUniqueId());
        }

        /**
         * Fetches similar entries from the database based on the input.
         *
         * @param input the input string to search for
         * @return a sorted list of similar entries, limited to 10 results
         */
        public static List<String> fetchSimilarNames(@Nullable String input) {
            if (input == null || input.trim().isBlank())
                return new ArrayList<>();

            final String inputTrimmed = input.trim().toLowerCase();
            final String searchPattern = "%" + inputTrimmed + "%";

            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                return context
                    .select(NICKNAMES.NICKNAME.as("suggestion"))
                    .from(NICKNAMES)
                    .where(lower(NICKNAMES.NICKNAME).like(searchPattern).and(NICKNAMES.NICKNAME.isNotNull()))
                    .unionAll(
                        context.select(NICKNAMES.USERNAME.as("suggestion"))
                            .from(NICKNAMES)
                            .where(lower(NICKNAMES.USERNAME).like(searchPattern))
                    )
                    .orderBy(
                        field("suggestion").asc()
                    )
                    .limit(20)
                    .fetch()
                    .map(Record1::value1);
            } catch (SQLException ignored) {
            }
            return List.of();
        }

        /**
         * Fetches the most similar nickname from the database based on the input.
         * <br><br>
         * The method first tries to find an exact match for the input. If no exact match is found,
         * it searches for nicknames or usernames that contain the input string, picking the most similar.
         * <br><br>
         * Matching/similar nicknames are prioritized over usernames.
         *
         * @param input the input string to search for
         * @return a database nickname if found, otherwise an empty optional
         */
        public static Optional<io.github.milkdrinkers.maquillage.module.nickname.Nickname> fetchMostSimilarNickname(@Nullable String input) {
            if (input == null || input.trim().isBlank())
                return Optional.empty();

            final String inputTrimmed = input.trim().toLowerCase();
            final String searchPattern = "%" + inputTrimmed + "%";

            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                // Try to get exact match first
                final @NotNull Optional<io.github.milkdrinkers.maquillage.module.nickname.Nickname> exactMatch = context
                    .selectFrom(NICKNAMES)
                    .where(lower(NICKNAMES.NICKNAME).eq(inputTrimmed)
                        .and(NICKNAMES.NICKNAME.isNotNull())
                        .or(lower(NICKNAMES.USERNAME).eq(inputTrimmed))
                    )
                    .fetchOptional()
                    .map(record -> new io.github.milkdrinkers.maquillage.module.nickname.Nickname(
                        record.getNickname(),
                        record.getUsername()
                    ));

                if (exactMatch.isPresent())
                    return exactMatch;

                // Try to find best similar match
                final @NotNull Optional<io.github.milkdrinkers.maquillage.module.nickname.Nickname> similarMatch = context
                    .selectFrom(NICKNAMES)
                    .where(lower(NICKNAMES.NICKNAME).like(searchPattern)
                        .and(NICKNAMES.NICKNAME.isNotNull())
                        .or(lower(NICKNAMES.USERNAME).like(searchPattern))
                    )
                    .orderBy(
                        case_().when(lower(NICKNAMES.NICKNAME).like(searchPattern), 1) // Prioritize nicknames that match over usernames
                            .else_(2),
                        NICKNAMES.NICKNAME.asc() // Sort alphabetically
                    )
                    .limit(1)
                    .fetchOptional()
                    .map(record -> new io.github.milkdrinkers.maquillage.module.nickname.Nickname(
                        record.getNickname(),
                        record.getUsername()
                    ));

                return similarMatch;
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
            }
            return Optional.empty();
        }
    }

    /**
     * Holds all queries related to using the database as a messaging service.
     */
    @ApiStatus.Internal
    public static final class Sync {
        /**
         * Fetch the latest (greatest) message ID from the database.
         *
         * @return the message id or empty if no messages are queued
         */
        public static Optional<Integer> fetchLatestMessageId() {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                return context
                    .select(max(MESSAGING.ID))
                    .from(MESSAGING)
                    .fetchOptional(0, Integer.class);
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
                return Optional.empty();
            }
        }

        /**
         * Adds a message to the database.
         *
         * @param message the outgoing message to send
         * @return the new message id or empty if insert failed
         */
        public static <T> Optional<Integer> send(OutgoingMessage<T> message) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                return context
                    .insertInto(MESSAGING, MESSAGING.TIMESTAMP, MESSAGING.MESSAGE)
                    .values(
                        currentLocalDateTime(),
                        val(message.encode())
                    )
                    .returningResult(MESSAGING.ID)
                    .fetchOptional(0, Integer.class);
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
                return Optional.empty();
            }
        }

        /**
         * Fetch all messages from the database.
         *
         * @param latestSyncId    the currently synced to message id
         * @param cleanupInterval the configured cleanup interval
         * @return the messages
         */
        public static Map<Integer, IncomingMessage<?, ?>> receive(int latestSyncId, long cleanupInterval) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                return context
                    .selectFrom(MESSAGING)
                    .where(MESSAGING.ID.greaterThan(latestSyncId)
                        .and(MESSAGING.TIMESTAMP.greaterOrEqual(localDateTimeSub(currentLocalDateTime(), cleanupInterval / 1000, DatePart.SECOND))) // Checks TIMESTAMP >= now() - cleanupInterval
                    )
                    .orderBy(MESSAGING.ID.asc())
                    .fetch()
                    .intoMap(MESSAGING.ID, r -> BidirectionalMessage.from(r.getMessage()));
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
                return Map.of();
            }
        }

        /**
         * Deletes all outdate messages from the database.
         *
         * @param cleanupInterval the configured cleanup interval
         */
        public static void cleanup(long cleanupInterval) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context
                    .deleteFrom(MESSAGING)
                    .where(MESSAGING.TIMESTAMP.lessThan(localDateTimeSub(currentLocalDateTime(), cleanupInterval / 1000, DatePart.SECOND))) // Checks TIMESTAMP < now() - cleanupInterval
                    .execute();
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!" + e);
            }
        }
    }

    /**
     * Wrapper class to organize cooldown-related queries.
     */
    public static final class Cooldown {
        public static Map<CooldownType, Instant> load(OfflinePlayer player) {
            return load(player.getUniqueId());
        }

        public static Map<CooldownType, Instant> load(UUID uuid) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                final Result<CooldownsRecord> cooldownsRecords = context
                    .selectFrom(COOLDOWNS)
                    .where(COOLDOWNS.UUID.eq(UUIDUtil.toBytes(uuid)))
                    .fetch();

                return cooldownsRecords.stream()
                    .collect(Collectors.toMap(
                        r -> CooldownType.valueOf(r.getCooldownType()),
                        r -> QueryUtils.InstantUtil.fromDateTime(r.getCooldownTime())
                    ));
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
            }
            return Collections.emptyMap();
        }

        public static void save(OfflinePlayer player) {
            save(player.getUniqueId());
        }

        public static void save(UUID uuid) {
            try (
                Connection con = DB.getConnection()
            ) {
                DSLContext context = DB.getContext(con);

                context.transaction(config -> {
                    DSLContext ctx = config.dsl();

                    // Delete old cooldowns
                    ctx.deleteFrom(COOLDOWNS)
                        .where(COOLDOWNS.UUID.eq(UUIDUtil.toBytes(uuid)))
                        .execute();

                    // Insert new cooldowns
                    final List<CooldownsRecord> cooldownsRecords = new ArrayList<>();

                    for (CooldownType cooldownType : CooldownType.values()) {
                        if (!Cooldowns.has(uuid, cooldownType))
                            continue;

                        cooldownsRecords.add(new CooldownsRecord(
                            UUIDUtil.toBytes(uuid),
                            cooldownType.name(),
                            QueryUtils.InstantUtil.toDateTime(Cooldowns.get(uuid, cooldownType))
                        ));
                    }

                    if (!cooldownsRecords.isEmpty())
                        ctx.batchInsert(cooldownsRecords).execute();
                });
            } catch (SQLException e) {
                Logger.get().error("SQL Query threw an error!", e);
            }
        }
    }
}
