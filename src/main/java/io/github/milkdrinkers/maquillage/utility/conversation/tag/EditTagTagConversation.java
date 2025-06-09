package io.github.milkdrinkers.maquillage.utility.conversation.tag;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditTagTagConversation {

    static String currentTag;
    static String updatedTag;
    static Tag tag;

    public static Prompt editTagPrompt(Tag tag) {
        EditTagTagConversation.tag = tag;
        EditTagTagConversation.currentTag = tag.getTag();
        return editTagStringPrompt;
    }

    static Prompt editTagStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.tag.current-tag"))
                .with("tag", currentTag).build());
            return Translation.of("commands.module.tag.edit.tag.current-tag-question");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            updatedTag = s;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = TagHolder.getInstance().update(updatedTag, tag.getPerm(), tag.getLabel(), tag.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.tag.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.tag.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.tag.not-updated")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.tag.confirm"))
                .with("tag", updatedTag).build());
            return "YES/NO";
        }
    };
}
