package io.github.Alathra.Maquillage.utility.conversations;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.Tag;
import io.github.Alathra.Maquillage.tag.TagHandler;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TagConversation {

    static String tag;
    static String permission;
    static String name;
    static String identifier;

    public static Prompt newTagPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the desired tag.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            tag = input;
            return namePrompt;
        }
    };

    static Prompt namePrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Input the display name.";
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            name = input;
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
                int ID = TagHandler.addTag(tag, permission, name, identifier);
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
            player.sendMessage(ColorParser.of("Do you want to save the tag " + tag + "<white> with the display name " + name + ", the identifier " + identifier + " and the permission node " + permission + "?").build());
            return "YES/NO?";
        }
    };
}
