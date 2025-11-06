package io.github.milkdrinkers.maquillage.messaging.adapter.receiver;

import io.github.milkdrinkers.maquillage.messaging.message.IncomingMessage;

import java.util.function.Consumer;

/**
 * Defines platform specific behavior when receiving a message from the message broker.
 */
public abstract class ReceiverAdapter implements Consumer<IncomingMessage<?, ?>> {
    @Override
    public abstract void accept(final IncomingMessage<?, ?> message);
}
