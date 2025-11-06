package io.github.milkdrinkers.maquillage.listener.listeners;

import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.database.schema.Tables;
import io.github.milkdrinkers.maquillage.messaging.adapter.receiver.event.MessageReceivedEvent;
import io.github.milkdrinkers.maquillage.messaging.message.IncomingMessage;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorBuilder;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagBuilder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.concurrent.CompletableFuture;

import static io.github.milkdrinkers.maquillage.messaging.MessagingUtils.*;

/**
 * Handles responding to Maquillage instance synchronization requests over message handler.
 */
public class MessageReceivedListener implements Listener {
    @SuppressWarnings("unused")
    @EventHandler
    public void onMessage(MessageReceivedEvent e) {
        final IncomingMessage<?, ?> msg = e.getMessage();

        if (msg.getPayload() instanceof NameColorFetchMessage(int data)) {
            CompletableFuture
                .supplyAsync(() -> Queries.NameColor.loadColor(data))
                .thenAccept(r -> {
                    if (r == null)
                        throw new IllegalStateException("Error while fetching color, r is null!");

                    final NameColor nameColor = new NameColorBuilder()
                        .withColor(r.get(Tables.COLORS.COLOR))
                        .withPerm(r.get(Tables.COLORS.PERM))
                        .withLabel(r.get(Tables.COLORS.LABEL))
                        .withDatabaseId(data)
                        .createNameColor();

                    NameColorHolder.getInstance().load(nameColor);
                });
        } else if (msg.getPayload() instanceof NameColorDeleteMessage(int data)) {
            NameColorHolder.getInstance().cacheRemove(data);
        } else if (msg.getPayload() instanceof TagFetchMessage(int data)) {
            CompletableFuture
                .supplyAsync(() -> Queries.Tag.loadTag(data))
                .thenAccept(r -> {
                    if (r == null)
                        throw new IllegalStateException("Error while fetching tag, r is null!");

                    final Tag tag = new TagBuilder()
                        .withTag(r.get(Tables.TAGS.TAG))
                        .withPerm(r.get(Tables.TAGS.PERM))
                        .withLabel(r.get(Tables.TAGS.LABEL))
                        .withDatabaseId(data)
                        .createTag();

                    TagHolder.getInstance().load(tag);
                });
        } else if (msg.getPayload() instanceof TagDeleteMessage(int data)) {
            TagHolder.getInstance().cacheRemove(data);
        }
    }
}
