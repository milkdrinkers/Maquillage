package io.github.Alathra.Maquillage.db.sync;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.db.schema.Tables;
import io.github.Alathra.Maquillage.module.namecolor.NameColor;
import io.github.Alathra.Maquillage.module.namecolor.NameColorBuilder;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.Tag;
import io.github.Alathra.Maquillage.module.tag.TagBuilder;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import io.github.Alathra.Maquillage.utility.Logger;
import org.bukkit.Bukkit;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class SyncHandler implements Reloadable {
    /**
     * On plugin load.
     */
    @Override
    public void onLoad() {

    }

    /**
     * On plugin enable.
     */
    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskTimer(Maquillage.getInstance(), this::sync, 30, 30);
//        Bukkit.getScheduler().runTaskTimerAsynchronously(Maquillage.getInstance(), this::runCleanUp, 120, 120); // TODO Re-implement, not working
    }

    /**
     * On plugin disable.
     */
    @Override
    public void onDisable() {

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
        DatabaseQueries.cleanUpSyncMessages();
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
                            fetchColor(Integer.parseInt(message.substring(SyncType.COLOR.name().length() + 1))).thenAccept(r -> {
                                if (r == null)
                                    throw new IllegalStateException("Error while fetching color, r is null!");

                                NameColor nameColor = new NameColorBuilder()
                                    .withColor(r.get(Tables.COLORS.COLOR))
                                    .withPerm(r.get(Tables.COLORS.PERM))
                                    .withName(r.get(Tables.COLORS.DISPLAYNAME))
                                    .withIdentifier(r.get(Tables.COLORS.IDENTIFIER))
                                    .withID(r.get(Tables.COLORS.ID))
                                    .createNameColor();

                                NameColorHolder.getInstance().load(nameColor);
                            });
                        } else if (message.startsWith(SyncType.TAG.name())) {
                            Logger.get().info(String.valueOf(Integer.parseInt(message.substring(SyncType.TAG.name().length() + 1))));

                            fetchTag(Integer.parseInt(message.substring(SyncType.TAG.name().length() + 1))).thenAccept(r -> {
                                Logger.get().info("SOME FUCKING LOG #1");
                                if (r == null)
                                    throw new IllegalStateException("Error while fetching tag, r is null!");

                                Logger.get().info("SOME FUCKING LOG #2");
                                Tag tag = new TagBuilder()
                                    .withTag(r.get(Tables.TAGS.TAG))
                                    .withPerm(r.get(Tables.TAGS.PERM))
                                    .withName(r.get(Tables.TAGS.DISPLAYNAME))
                                    .withIdentifier(r.get(Tables.TAGS.IDENTIFIER))
                                    .withID(r.get(Tables.TAGS.ID))
                                    .createTag();
                                Logger.get().info("SOME FUCKING LOG #3");

                                TagHolder.getInstance().load(tag);
                                Logger.get().info("SOME FUCKING LOG #4");
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
        return CompletableFuture.supplyAsync(() -> DatabaseQueries.fetchSyncMessages(latestSyncId));
    }

    private CompletableFuture<Record> fetchColor(final int id) {
        return CompletableFuture.supplyAsync(() -> DatabaseQueries.loadColor(id));
    }

    private CompletableFuture<Record> fetchTag(final int id) {
        return CompletableFuture.supplyAsync(() -> DatabaseQueries.loadTag(id));
    }

    public int getLatestSyncId() {
        return this.latestSyncId;
    }

    public void setLatestSyncId(int latestSyncId) {
        this.latestSyncId = latestSyncId;
    }

    public void saveSyncMessage(SyncAction action, SyncType type, final int id) {
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> DatabaseQueries.saveSyncMessage(action.name() + " " + type.name() + " " + id));
    }

}
