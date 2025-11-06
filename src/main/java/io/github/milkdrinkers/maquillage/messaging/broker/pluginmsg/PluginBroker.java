package io.github.milkdrinkers.maquillage.messaging.broker.pluginmsg;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.messaging.MessageConsumer;
import io.github.milkdrinkers.maquillage.messaging.broker.AbstractBroker;
import io.github.milkdrinkers.maquillage.messaging.broker.MessagingUtils;
import io.github.milkdrinkers.maquillage.messaging.config.MessagingConfig;
import io.github.milkdrinkers.maquillage.messaging.message.BidirectionalMessage;
import io.github.milkdrinkers.maquillage.messaging.message.OutgoingMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Implementation of the plugin messaging system as a message broker
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public final class PluginBroker extends AbstractBroker implements PluginMessageListener {
    private final Maquillage plugin;
    private final String name;
    private final String channelName;

    public PluginBroker(MessageConsumer messageConsumer, String name) {
        super(messageConsumer);
        this.plugin = Maquillage.getInstance();
        this.name = name;
        this.channelName = "%s:message".formatted(name.toLowerCase());
    }

    @Override
    public <T> void send(@NotNull OutgoingMessage<T> message) {
        send(MessagingUtils.ByteUtil.to(message));
    }

    /**
     * Schedules a task to send a plugin message via the first available player
     *
     * @param messageBytes the encoded message
     */
    private void send(byte[] messageBytes) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, task -> {
            final Optional<Player> playerEntry = plugin.getServer().getOnlinePlayers().stream().findFirst().map(Player::getPlayer);
            if (playerEntry.isEmpty())
                return;

            playerEntry.get().sendPluginMessage(plugin, channelName, messageBytes);
            task.cancel();
        }, 1L, 100L);
    }

    private void receive(byte[] messageBytes) {
        final BidirectionalMessage<?> message = MessagingUtils.ByteUtil.from(messageBytes);
        getMessageConsumer().consumeMessage(message);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(channelName))
            return;

        receive(message);
    }

    @Override
    public void init(MessagingConfig config) throws IOException, InterruptedException, NoSuchAlgorithmException {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channelName);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channelName, this);
    }

    @Override
    public void close() {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, channelName);
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, channelName);
    }
}
