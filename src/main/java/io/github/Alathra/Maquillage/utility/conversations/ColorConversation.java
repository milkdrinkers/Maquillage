package io.github.Alathra.Maquillage.utility.conversations;

import com.github.milkdrinkers.colorparser.ColorParser;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorConversation {

    static String color;
    static String permission;

    public static Prompt newColorPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired color.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            color = input;
            return permissionPrompt;
        }
    };

    static Prompt permissionPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired permission node.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            permission = input;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            if (input.equals("YES")) {
                return Prompt.END_OF_CONVERSATION;
            }
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            Conversable conversable = context.getForWhom();
            if (conversable instanceof Player) {
                Player player = (Player) conversable;
                String colorName = color + player.getName();
                player.sendMessage(ColorParser.of("Do you want to save this color " + colorName + "<white> with the permission node " + permission + "?").build());
                return "YES/NO?";
            }
            conversable.sendRawMessage("Do you want to save the tag" + ColorParser.of(color).build() + " with the permission node " + permission + "?");
            return "YES/NO?";
        }
    };

}
