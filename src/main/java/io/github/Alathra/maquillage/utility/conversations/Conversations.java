package io.github.alathra.maquillage.utility.conversations;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.jetbrains.annotations.NotNull;

public class Conversations {

    public static ConversationPrefix prefix = new ConversationPrefix() {
        @Override
        public @NotNull String getPrefix(@NotNull ConversationContext context) {
            return "[Maquillage] ";
        }
    };

}
