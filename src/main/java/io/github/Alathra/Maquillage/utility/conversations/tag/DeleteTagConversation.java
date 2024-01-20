package io.github.Alathra.Maquillage.utility.conversations.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.tag.Tag;
import io.github.Alathra.Maquillage.tag.TagHandler;
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
                boolean success = TagHandler.removeTag(tag);
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The tag was successfully deleted!").build());

                    TagHandler.clearALlPlayerTagsWithID(tag.getID());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The tag was not deleted.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The tag was not deleted.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Are you sure you want to delete this tag: " + tag.getTag() + "<white>?").build());
            player.sendMessage(ColorParser.of("<red>This action is permanent.").build());
            return "YES/NO";
        }
    };

}
