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

public class EditColorLabelConversation {

    static String currentLabel;
    static String updatedLabel;
    static NameColor color;

    public static Prompt editNamePrompt(NameColor color) {
        EditColorLabelConversation.color = color;
        EditColorLabelConversation.currentLabel = color.getLabel();
        return editNameStringPrompt;
    }

    static Prompt editNameStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("The current label for " + color.getColor() + player.getName() + "<white> is " + currentLabel).build());
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
                boolean success = NameColorHolder.getInstance().update(color.getColor(), color.getPerm(), updatedLabel, color.getKey(), color.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The color label was successfully updated!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The color label was not updated.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The color label was not updated.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Do you want to update the label of this color " + color.getColor() + player.getName() + "<white> to " + updatedLabel + "?").build());
            return "YES/NO?";
        }
    };
}
