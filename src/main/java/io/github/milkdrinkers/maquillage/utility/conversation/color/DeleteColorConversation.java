package io.github.milkdrinkers.maquillage.utility.conversation.color;

import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeleteColorConversation {

    static NameColor color;

    public static Prompt confirmDeletePrompt(NameColor color) {
        DeleteColorConversation.color = color;
        return confirmPrompt;
    }

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = NameColorHolder.getInstance().remove(color);
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.delete.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.delete.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.delete.not-deleted")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.delete.confirm"))
                .parseMinimessagePlaceholder("namecolor", color.getColor() + player.getName()).build());
            return "YES/NO";
        }
    };

}
