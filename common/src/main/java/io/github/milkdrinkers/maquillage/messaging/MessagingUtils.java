package io.github.milkdrinkers.maquillage.messaging;

import io.github.milkdrinkers.maquillage.messaging.message.BidirectionalMessage;
import io.github.milkdrinkers.maquillage.messaging.message.MessageCodec;
import io.github.milkdrinkers.maquillage.utility.Messaging;

/**
 * Handles sending of Maquillage instance synchronization requests over message handler.
 */
public class MessagingUtils {
    private static final String CHANNEL = "maquillage";

    /**
     * Registers the codecs for every payload sent on the {@code maquillage} channel.
     *
     * <p>Must run before the messaging handler starts. Payloads are looked up by class name at
     * both ends, so an unregistered type fails on send and on receive alike.
     */
    public static void registerCodecs() {
        BidirectionalMessage.registerCodec(NameColorFetchMessage.CODEC);
        BidirectionalMessage.registerCodec(NameColorDeleteMessage.CODEC);
        BidirectionalMessage.registerCodec(TagFetchMessage.CODEC);
        BidirectionalMessage.registerCodec(TagDeleteMessage.CODEC);
    }

    public record NameColorFetchMessage(int data) {
        static final MessageCodec<NameColorFetchMessage> CODEC = MessageCodec.of(
            NameColorFetchMessage.class,
            (v, out) -> out.writeInt(v.data()),
            in -> new NameColorFetchMessage(in.readInt())
        );
    }

    public record NameColorDeleteMessage(int data) {
        static final MessageCodec<NameColorDeleteMessage> CODEC = MessageCodec.of(
            NameColorDeleteMessage.class,
            (v, out) -> out.writeInt(v.data()),
            in -> new NameColorDeleteMessage(in.readInt())
        );
    }

    public record TagFetchMessage(int data) {
        static final MessageCodec<TagFetchMessage> CODEC = MessageCodec.of(
            TagFetchMessage.class,
            (v, out) -> out.writeInt(v.data()),
            in -> new TagFetchMessage(in.readInt())
        );
    }

    public record TagDeleteMessage(int data) {
        static final MessageCodec<TagDeleteMessage> CODEC = MessageCodec.of(
            TagDeleteMessage.class,
            (v, out) -> out.writeInt(v.data()),
            in -> new TagDeleteMessage(in.readInt())
        );
    }

    public static void sendNameColorFetch(int id) {
        send(new NameColorFetchMessage(id));
    }

    public static void sendNameColorDelete(int id) {
        send(new NameColorDeleteMessage(id));
    }

    public static void sendTagFetch(int id) {
        send(new TagFetchMessage(id));
    }

    public static void sendTagDelete(int id) {
        send(new TagDeleteMessage(id));
    }

    private static <T> void send(T payload) {
        Messaging.send(BidirectionalMessage.<T>builder()
            .channelId(CHANNEL)
            .payload(payload)
            .build());
    }
}
