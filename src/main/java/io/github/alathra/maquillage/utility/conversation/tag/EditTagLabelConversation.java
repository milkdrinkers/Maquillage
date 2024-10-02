package io.github.alathra.maquillage.utility.conversation.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.module.cosmetic.tag.Tag;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditTagLabelConversation {

    static String currentLabel;
    static String updatedLabel;
    static Tag tag;

    public static Prompt editNamePrompt(Tag tag) {
        EditTagLabelConversation.tag = tag;
        EditTagLabelConversation.currentLabel = tag.getLabel();
        return editNameStringPrompt;
    }

    static Prompt editNameStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("The current name for " + tag.getTag() + player.getName() + "<white> is " + currentLabel).build());
            return "What do you want the new label to be?";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            updatedLabel = s;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = TagHolder.getInstance().update(tag.getTag(), tag.getPerm(), updatedLabel, tag.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The tag label was successfully updated!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The tag label was not updated.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The tag label was not updated.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Do you want to update the label of this tag " + tag.getTag() + "<white> to " + updatedLabel + "?").build());
            return "YES/NO?";
        }
    };
}
