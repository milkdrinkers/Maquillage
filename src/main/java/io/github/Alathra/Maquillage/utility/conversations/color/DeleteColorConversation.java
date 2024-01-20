package io.github.Alathra.Maquillage.utility.conversations.color;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeleteColorConversation {

    static NameColor color;

    public static Prompt confirmDeletePrompt(NameColor color) {
        DeleteColorConversation.color = color;
        return confirmPrompt;
    }

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = NameColorHandler.removeColor(color);
                if (success) {
                    player.sendMessage(ColorParser.of("<green>The color was successfully deleted!").build());
                } else {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The color was not deleted.").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The color was not deleted.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Are you sure you want to delete this color: " + color.getColor() + player.getName() + "<white>?").build());
            player.sendMessage(ColorParser.of("<red>This action is permanent.").build());
            return "YES/NO";
        }
    };

}
