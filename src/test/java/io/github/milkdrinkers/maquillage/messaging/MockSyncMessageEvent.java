package io.github.milkdrinkers.maquillage.messaging;

import io.github.milkdrinkers.maquillage.event.MockEvent;
import io.github.milkdrinkers.maquillage.messaging.message.IncomingMessage;

public class MockSyncMessageEvent extends MockEvent {
    private final IncomingMessage<?, ?> message;

    public MockSyncMessageEvent(IncomingMessage<?, ?> message) {
        this.message = message;
    }

    public IncomingMessage<?, ?> getMessage() {
        return message;
    }
}