package io.github.milkdrinkers.maquillage.messaging;

import io.github.milkdrinkers.maquillage.event.MockEventSystem;
import io.github.milkdrinkers.maquillage.messaging.adapter.receiver.ReceiverAdapter;
import io.github.milkdrinkers.maquillage.messaging.message.IncomingMessage;

public class MockReceiverAdapter extends ReceiverAdapter {
    @Override
    public void accept(IncomingMessage<?, ?> message) {
        MockEventSystem.fireEvent(new MockSyncMessageEvent(message));
    }
}
