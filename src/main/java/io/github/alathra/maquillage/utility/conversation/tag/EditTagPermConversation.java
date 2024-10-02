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

public class EditTagPermConversation {

    static String currentPerm;
    static String updatedPerm;
    static Tag tag;

    public static Prompt editPermPrompt(Tag tag) {
        EditTagPermConversation.tag = tag;
        EditTagPermConversation.currentPerm = tag.getPerm();
        return editPermStringPrompt;
    }

    static Prompt editPermStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();

            if (currentPerm.isEmpty()) {
                player.sendMessage(ColorParser.of("The tag " + tag.getTag() + "<white> is permissionless").build());
            } else {
                player.sendMessage(ColorParser.of("The current permission node for " + tag.getTag() + "<white> is " + currentPerm).build());
            }


            return "Input the desired permission node, or \"none\" for permissionless. The final permission node will be \"maquillage.tag.[your input]\"";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            if (s.toLowerCase().equals("none")){
                updatedPerm = "";
            } else {
                while (s.charAt(0) == '.') {
                    s = s.substring(1);
                }
                updatedPerm = s;
            }
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = TagHolder.getInstance().update(tag.getTag(), updatedPerm, tag.getLabel(), tag.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The permission node was successfully updated!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The permission node was not updated.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The permission node was not updated.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Do you want to update the permission node for " + tag.getTag() + "<white> to " + updatedPerm + "?").build());
            return "YES/NO?";
        }
    };
}
