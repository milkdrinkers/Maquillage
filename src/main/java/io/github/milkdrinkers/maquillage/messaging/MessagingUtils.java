package io.github.milkdrinkers.maquillage.messaging;

import com.google.gson.annotations.SerializedName;
import io.github.milkdrinkers.maquillage.messaging.message.BidirectionalMessage;
import io.github.milkdrinkers.maquillage.utility.Messaging;

/**
 * Handles sending of Maquillage instance synchronization requests over message handler.
 */
public class MessagingUtils {
    public record NameColorFetchMessage(@SerializedName("namecolor_id") int data) {
    }

    public record NameColorDeleteMessage(@SerializedName("namecolor_id") int data) {
    }

    public static void sendNameColorFetch(int id) {
        final NameColorFetchMessage payload = new NameColorFetchMessage(id);
        final BidirectionalMessage<Object> message = BidirectionalMessage.builder()
            .channelId("maquillage")
            .payload(payload)
            .build();
        Messaging.send(message);
    }

    public static void sendNameColorDelete(int id) {
        final NameColorDeleteMessage payload = new NameColorDeleteMessage(id);
        final BidirectionalMessage<Object> message = BidirectionalMessage.builder()
            .channelId("maquillage")
            .payload(payload)
            .build();
        Messaging.send(message);
    }

    public record TagFetchMessage(@SerializedName("tag_id") int data) {
    }

    public record TagDeleteMessage(@SerializedName("tag_id") int data) {
    }

    public static void sendTagFetch(int id) {
        final TagFetchMessage payload = new TagFetchMessage(id);
        final BidirectionalMessage<Object> message = BidirectionalMessage.builder()
            .channelId("maquillage")
            .payload(payload)
            .build();
        Messaging.send(message);
    }

    public static void sendTagDelete(int id) {
        final TagDeleteMessage payload = new TagDeleteMessage(id);
        final BidirectionalMessage<Object> message = BidirectionalMessage.builder()
            .channelId("maquillage")
            .payload(payload)
            .build();
        Messaging.send(message);
    }
}
