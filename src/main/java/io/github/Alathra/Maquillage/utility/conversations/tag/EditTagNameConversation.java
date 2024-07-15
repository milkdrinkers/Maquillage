package io.github.Alathra.Maquillage.utility.conversations.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.module.tag.Tag;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditTagNameConversation {

    static String currentName;
    static String updatedName;
    static Tag tag;

    public static Prompt editNamePrompt(Tag tag) {
        EditTagNameConversation.tag = tag;
        EditTagNameConversation.currentName = tag.getName();
        return editNameStringPrompt;
    }

    static Prompt editNameStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("The current name for " + tag.getTag() + player.getName() + "<white> is " + currentName).build());
            return "What do you want the new name to be?";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            updatedName = s;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = TagHolder.getInstance().update(tag.getTag(), tag.getPerm(), updatedName, tag.getIdentifier(), tag.getID());
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The tag name was successfully updated!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The tag name was not updated.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The tag name was not updated.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Do you want to update the name of this tag " + tag.getTag() + "<white> to " + updatedName + "?").build());
            return "YES/NO?";
        }
    };
}
