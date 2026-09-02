package io.github.milkdrinkers.maquillage.messaging.adapter.receiver;

import io.github.milkdrinkers.maquillage.messaging.adapter.receiver.event.MessageReceivedEvent;
import io.github.milkdrinkers.maquillage.messaging.message.Message;
import io.github.milkdrinkers.threadutil.Scheduler;

public class BukkitReceiverAdapter extends ReceiverAdapter {
    @Override
    public void accept(Message<?> message) {
        Scheduler.sync(() -> new MessageReceivedEvent(message).callEvent()).execute();
    }
}
