package io.github.milkdrinkers.maquillage.messaging;

import io.github.milkdrinkers.maquillage.event.MockEvent;
import io.github.milkdrinkers.maquillage.messaging.message.Message;

public class MockSyncMessageEvent extends MockEvent {
    private final Message<?> message;

    public MockSyncMessageEvent(Message<?> message) {
        this.message = message;
    }

    public Message<?> getMessage() {
        return message;
    }
}