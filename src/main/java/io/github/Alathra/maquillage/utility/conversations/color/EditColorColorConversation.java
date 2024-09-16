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

public class EditColorColorConversation {

    static String currentColor;
    static String updatedColor;
    static NameColor color;

    public static Prompt editColorPrompt(NameColor color) {
        EditColorColorConversation.color = color;
        EditColorColorConversation.currentColor = color.getColor();
        return editColorStringPrompt;
    }

    static Prompt editColorStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("The current color is " + currentColor + player.getName()).build());
            return "What do you want the new color to be?";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            updatedColor = s;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = NameColorHolder.getInstance().update(updatedColor, color.getPerm(), color.getLabel(), color.getKey(), color.getID());
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The color was successfully updated!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The color was not updated.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The color was not updated.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Do you want to update the color to this: " + updatedColor + player.getName() + "<white>?").build());
            return "YES/NO?";
        }
    };
}
