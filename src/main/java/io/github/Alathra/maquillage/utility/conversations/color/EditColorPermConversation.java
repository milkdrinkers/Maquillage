package io.github.alathra.maquillage.utility.conversations.color;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditColorPermConversation {

    static String currentPerm;
    static String updatedPerm;
    static NameColor color;

    public static Prompt editPermPrompt(NameColor color) {
        EditColorPermConversation.color = color;
        EditColorPermConversation.currentPerm = color.getPerm();
        return editPermStringPrompt;
    }

    static Prompt editPermStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("The current permission node for " + color.getColor() + player.getName() + "<white> is " + currentPerm).build());
            return "What do you want the new permission node to be?";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            updatedPerm = s;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = NameColorHolder.getInstance().update(color.getColor(), updatedPerm, color.getLabel(), color.getKey(), color.getDatabaseId());
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
            player.sendMessage(ColorParser.of("Do you want to update the permission node for " + color.getColor() + player.getName() + "<white> to " + updatedPerm + "?").build());
            return "YES/NO?";
        }
    };
}
