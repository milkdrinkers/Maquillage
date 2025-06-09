package io.github.milkdrinkers.maquillage.utility.conversation.tag;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeleteTagConversation {

    static Tag tag;

    public static Prompt confirmDeletePrompt(Tag tag) {
        DeleteTagConversation.tag = tag;
        return confirmPrompt;
    }

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = TagHolder.getInstance().remove(tag);
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.delete.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.delete.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.delete.not-deleted")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.delete.confirm"))
                .with("tag", tag.getTag()).build());
            return "YES/NO";
        }
    };

}
