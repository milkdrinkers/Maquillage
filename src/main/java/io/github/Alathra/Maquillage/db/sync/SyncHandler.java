package io.github.Alathra.Maquillage.db.sync;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.db.schema.Tables;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import org.bukkit.Bukkit;
import org.jooq.Record3;
import org.jooq.Record;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class SyncHandler {

    public enum SyncAction {
        FETCH,
        DELETE
    }

    public enum SyncType {
        COLOR,
        TAG
    }

    private int latestSyncId = -1;

    public SyncHandler() {
        Bukkit.getScheduler().runTaskTimer(Maquillage.getInstance(), this::sync, 30, 30);
        Bukkit.getScheduler().runTaskTimerAsynchronously(Maquillage.getInstance(), this::runCleanUp, 120, 120);
    }

    private void runCleanUp() {
        DatabaseQueries.cleanUpSyncMessages();
    }

    private void sync() {
        fetchSyncMessages().thenAccept(result -> {
            if (result == null)
                return;

            for (Record3<Integer, String, LocalDateTime> record : result) {
                if (record == null)
                    continue;

                String message = record.get(Tables.SYNC.MESSAGE);

                if (message.startsWith(SyncAction.FETCH.name())) {
                    message = message.substring(SyncAction.FETCH.name().length() + 1);

                    if (message.startsWith(SyncType.COLOR.name())) {
                        fetchColor(Integer.parseInt(message.substring(SyncType.COLOR.name().length() + 1))).thenAccept(r -> {
                            if (r == null)
                                return;

                            NameColorHandler.loadColor(r.get(Tables.COLORS.ID),
                                r.get(Tables.COLORS.COLOR),
                                r.get(Tables.COLORS.PERM),
                                r.get(Tables.COLORS.DISPLAYNAME),
                                r.get(Tables.COLORS.IDENTIFIER));
                        });
                    }

                    if (message.startsWith(SyncType.TAG.name())) {
                        fetchTag(Integer.parseInt(message.substring(SyncType.TAG.name().length() + 1))).thenAccept(r -> {
                            if (r == null)
                                return;

                            TagHandler.loadTag(r.get(Tables.TAGS.ID),
                                r.get(Tables.TAGS.TAG),
                                r.get(Tables.TAGS.PERM),
                                r.get(Tables.TAGS.DISPLAYNAME),
                                r.get(Tables.TAGS.IDENTIFIER));
                        });
                    }

                }

                if (message.startsWith(SyncAction.DELETE.name())) {
                    message = message.substring(SyncAction.DELETE.name().length() + 1);

                    if (message.startsWith(SyncType.COLOR.name())) {
                        NameColorHandler.uncacheColor(Integer.parseInt(message.substring(SyncType.COLOR.name().length() + 1)));
                    }

                    if (message.startsWith(SyncType.TAG.name())) {
                        TagHandler.uncacheTag(Integer.parseInt(message.substring(SyncType.TAG.name().length() + 1)));
                    }
                }

                this.latestSyncId = record.getValue(Tables.SYNC.ID);
            }
        });
    }

    private CompletableFuture<Result<Record3<Integer, String, LocalDateTime>>> fetchSyncMessages() {
        return CompletableFuture.supplyAsync(() -> DatabaseQueries.fetchSyncMessages(latestSyncId));
    }

    private CompletableFuture<Record> fetchColor(int id) {
        return CompletableFuture.supplyAsync(() -> DatabaseQueries.loadColor(id));
    }

    private CompletableFuture<Record> fetchTag(int id) {
        return CompletableFuture.supplyAsync(() -> DatabaseQueries.loadTag(id));
    }

    public int getLatestSyncId() {
        return this.latestSyncId;
    }

    public void setLatestSyncId(int latestSyncId) {
        this.latestSyncId = latestSyncId;
    }

    public void saveSyncMessage(SyncAction action, SyncType type, int id) {
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> DatabaseQueries.saveSyncMessage(action.name() + " " + type.name() + " " + id));
    }

}
