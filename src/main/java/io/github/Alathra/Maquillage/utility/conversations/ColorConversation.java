package io.github.Alathra.Maquillage.utility.conversations;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
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

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            Conversable conversable = context.getForWhom();
            Player player = (Player) conversable;
            if (input.equalsIgnoreCase("YES")) {
                int ID = NameColorHandler.addColor(new NameColor(color, permission));
                if (ID != -1) {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The color was not saved.").build());
                } else {
                    player.sendMessage(ColorParser.of("<green>The color was successfully saved!").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The color was not saved.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            Conversable conversable = context.getForWhom();
            Player player = (Player) conversable;
            String colorName = color + player.getName();
            player.sendMessage(ColorParser.of("Do you want to save this color " + colorName + "<white> with the permission node " + permission + "?").build());
            return "YES/NO?";
        }
    };

}
