package io.github.Alathra.Maquillage.utility.conversations.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.tag.Tag;
import io.github.Alathra.Maquillage.tag.TagHandler;
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
            player.sendMessage(ColorParser.of("The current tag is " + currentTag).build());
            return "What do you want the new tag to be?";
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
                boolean success = TagHandler.updateTag(updatedTag, tag.getPerm(), tag.getName(), tag.getIdentifier(), tag.getID());
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The tag was successfully updated!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The tag was not updated.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The tag was not updated.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Do you want to update the tag to this: " + updatedTag + "<white>?").build());
            return "YES/NO?";
        }
    };
}
