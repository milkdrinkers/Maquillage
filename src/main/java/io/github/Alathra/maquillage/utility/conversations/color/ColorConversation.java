package io.github.alathra.maquillage.utility.conversations.color;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorConversation {

    static String color;
    static String permission;
    static String label;
    static String identifier;
    static boolean colorIsGradient;
    static boolean colorIsRainbow;

    public static Prompt newColorPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired color.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            color = input;
            if (input.startsWith("<gradient")) {
                colorIsGradient = true;
                colorIsRainbow = false;
            } else if (input.startsWith("<rainbow")) {
                colorIsGradient = false;
                colorIsRainbow = true;
            } else {
                colorIsGradient = false;
                colorIsRainbow = false;
            }
            return labelPrompt;
        }
    };

    static Prompt labelPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired label.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            label = input;
            return identifierPrompt;
        }
    };

    static Prompt identifierPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            return "Input the identifier.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String input) {
            if (NameColorHolder.getInstance().doesIdentifierExist(input)) {
                Player player = (Player) conversationContext.getForWhom();
                player.sendMessage(ColorParser.of("<red>This identifier is already in use. Identifiers have to be unique").build());
                return identifierPrompt;
            }
            identifier = input;
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
                int ID = NameColorHolder.getInstance().add(color, permission, label, identifier);
                if (ID == -1) {
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

            // Adds ending to correctly display rainbows and gradients.
            String correctGradients = "";
            if (colorIsGradient)
                correctGradients = "</gradient>";
            if (colorIsRainbow)
                correctGradients = "</rainbow>";

            player.sendMessage(ColorParser.of("Do you want to save this color " + colorName + correctGradients + "<white> with the label " + label + ", the identifier " + identifier + " and the permission node " + permission + "?").build());
            return "YES/NO?";
        }
    };

}
