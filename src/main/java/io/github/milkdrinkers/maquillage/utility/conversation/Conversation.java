package io.github.milkdrinkers.maquillage.utility.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.jetbrains.annotations.NotNull;

public class Conversation {

    public static ConversationPrefix prefix = new ConversationPrefix() {
        @Override
        public @NotNull String getPrefix(@NotNull ConversationContext context) {
            return "[Maquillage] ";
        }
    };

}
