package io.github.alathra.maquillage.utility.conversation.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TagConversation {

    static String tag;
    static String permission;
    static String label;

    public static Prompt newTagPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired tag.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            tag = input;
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
            return permissionPrompt;
        }
    };

    static Prompt permissionPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired permission node, or \"none\" for permissionless. The final permission node will be \"maquillage.tag.[your input]\"";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            if (input.toLowerCase().equals("none")){
                permission = "";
            } else {
                while (input.charAt(0) == '.') {
                    input = input.substring(1);
                }
                permission = "maquillage.tag." + input;
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
                int ID = TagHolder.getInstance().add(tag, permission, label);
                if (ID == -1) {
                    player.sendMessage(ColorParser.of("<red>Something went wrong. The tag was not saved.").build());
                } else {
                    player.sendMessage(ColorParser.of("<green>The tag was successfully saved!").build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>The tag was not saved.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            Conversable conversable = context.getForWhom();
            Player player = (Player) conversable;
            player.sendMessage(ColorParser.of("Do you want to save the tag " + tag + "<white> with the display name " + label + " and the permission node " + permission + "?").build());
            return "YES/NO?";
        }
    };
}
