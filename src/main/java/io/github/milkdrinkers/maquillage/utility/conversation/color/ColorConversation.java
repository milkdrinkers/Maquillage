package io.github.milkdrinkers.maquillage.utility.conversation.color;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorConversation {

    static String color;
    static String permission;
    static String label;
    static boolean colorIsGradient;
    static boolean colorIsRainbow;

    public static Prompt newColorPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return Translation.of("commands.module.namecolor.create.input-namecolor");
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
            return Translation.of("commands.module.namecolor.create.input-label");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            label = input;
            return permissionPrompt;
        }
    };

    static Prompt permissionPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return Translation.of("commands.module.namecolor.create.input-perm");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            if (input.equalsIgnoreCase("none")) {
                permission = "";
            } else {
                while (input.charAt(0) == '.') {
                    input = input.substring(1);
                }
                permission = input;
            }
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            Conversable conversable = context.getForWhom();
            Player player = (Player) conversable;
            if (input.equalsIgnoreCase("YES")) {
                int ID = NameColorHolder.getInstance().add(color, permission, label, 0);
                if (ID == -1) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.create.failure")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.create.success")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.create.not-saved")).build());
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

            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.create.confirm"))
                .with("namecolor", colorName + correctGradients)
                .with("label", label)
                .with("perm", permission).build());
            return "YES/NO?";
        }
    };

}
