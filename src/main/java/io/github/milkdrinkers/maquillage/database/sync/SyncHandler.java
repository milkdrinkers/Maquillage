package io.github.milkdrinkers.maquillage.database.sync;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.database.schema.Tables;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorBuilder;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagBuilder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.utility.Logger;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class SyncHandler implements Reloadable {
    private BukkitTask syncTask = null;
    private BukkitTask cleanupTask = null;

    /**
     * On plugin load.
     */
    @Override
    public void onLoad(Maquillage plugin) {

    }

    /**
     * On plugin enable.
     */
    @Override
    public void onEnable(Maquillage plugin) {
        syncTask = Bukkit.getScheduler().runTaskTimer(Maquillage.getInstance(), this::sync, 600, 600);
        cleanupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Maquillage.getInstance(), this::runCleanUp, 2400, 2400);
    }

    /**
     * On plugin disable.
     */
    @Override
    public void onDisable(Maquillage plugin) {
        if (syncTask != null && !syncTask.isCancelled())
            syncTask.cancel();

        if (cleanupTask != null && !cleanupTask.isCancelled())
            cleanupTask.cancel();
    }

    public enum SyncAction {
        FETCH,
        DELETE
    }

    public enum SyncType {
        COLOR,
        TAG
    }

    private int latestSyncId = -1;

    private void runCleanUp() {
        Queries.Sync.cleanUpSyncMessages();
    }

    private void sync() {
        try {
            fetchSyncMessages().thenAccept(result -> {
                if (result == null)
                    throw new IllegalStateException("Error while syncing, result is null!");

                if (result.isEmpty())
                    return;

                for (Record3<Integer, String, LocalDateTime> record : result) {
                    if (record == null)
                        throw new IllegalStateException("Error while syncing, record is null!");

                    String message = record.get(Tables.SYNC.MESSAGE);

                    if (message.startsWith(SyncAction.FETCH.name())) {
                        message = message.substring(SyncAction.FETCH.name().length() + 1);

                        if (message.startsWith(SyncType.COLOR.name())) {
                            final int ID = Integer.parseInt(message.substring(SyncType.COLOR.name().length() + 1));

                            fetchColor(ID).thenAccept(r -> {
                                if (r == null)
                                    throw new IllegalStateException("Error while fetching color, r is null!");

                                NameColor nameColor = new NameColorBuilder()
                                    .withColor(r.get(Tables.COLORS.COLOR))
                                    .withPerm(r.get(Tables.COLORS.PERM))
                                    .withLabel(r.get(Tables.COLORS.LABEL))
                                    .withDatabaseId(ID)
                                    .createNameColor();

                                NameColorHolder.getInstance().load(nameColor);
                            });
                        } else if (message.startsWith(SyncType.TAG.name())) {
                            final int ID = Integer.parseInt(message.substring(SyncType.TAG.name().length() + 1));

                            fetchTag(ID).thenAccept(r -> {
                                if (r == null)
                                    throw new IllegalStateException("Error while fetching tag, r is null!");

                                Tag tag = new TagBuilder()
                                    .withTag(r.get(Tables.TAGS.TAG))
                                    .withPerm(r.get(Tables.TAGS.PERM))
                                    .withLabel(r.get(Tables.TAGS.LABEL))
                                    .withDatabaseId(ID)
                                    .createTag();

                                TagHolder.getInstance().load(tag);
                            });
                        }

                    } else if (message.startsWith(SyncAction.DELETE.name())) {
                        message = message.substring(SyncAction.DELETE.name().length() + 1);

                        if (message.startsWith(SyncType.COLOR.name())) {
                            NameColorHolder.getInstance().cacheRemove(Integer.parseInt(message.substring(SyncType.COLOR.name().length() + 1)));
                        }

                        if (message.startsWith(SyncType.TAG.name())) {
                            TagHolder.getInstance().cacheRemove(Integer.parseInt(message.substring(SyncType.TAG.name().length() + 1)));
                        }
                    }

                    this.latestSyncId = record.getValue(Tables.SYNC.ID);
                }
            });
        } catch (IllegalStateException e) {
            Logger.get().error("Something went wrong syncing: " + e);
        }
    }

    private CompletableFuture<Result<Record3<Integer, String, LocalDateTime>>> fetchSyncMessages() {
        return CompletableFuture.supplyAsync(() -> Queries.Sync.fetchSyncMessages(latestSyncId));
    }

    private CompletableFuture<Record> fetchColor(final int id) {
        return CompletableFuture.supplyAsync(() -> Queries.NameColor.loadColor(id));
    }

    private CompletableFuture<Record> fetchTag(final int id) {
        return CompletableFuture.supplyAsync(() -> Queries.Tag.loadTag(id));
    }

    public int getLatestSyncId() {
        return this.latestSyncId;
    }

    public void setLatestSyncId(int latestSyncId) {
        this.latestSyncId = latestSyncId;
    }

    public void saveSyncMessage(final SyncAction action, final SyncType type, final int id) {
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> Queries.Sync.saveSyncMessage(action, type, id));
    }
}
