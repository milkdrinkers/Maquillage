package io.github.alathra.maquillage.utility.conversation.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import io.github.alathra.maquillage.translation.Translation;
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
            return Translation.of("commands.module.tag.create.input-tag");
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
            return Translation.of("commands.module.tag.create.input-label");
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
            return Translation.of("commands.module.tag.create.input-perm");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            if (input.toLowerCase().equals("none")){
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
                int ID = TagHolder.getInstance().add(tag, permission, label);
                if (ID == -1) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.create.failure")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.create.success")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.create.not-saved")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            Conversable conversable = context.getForWhom();
            Player player = (Player) conversable;
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.create.input-tag"))
                .parseMinimessagePlaceholder("tag", tag)
                .parseMinimessagePlaceholder("label", label)
                .parseMinimessagePlaceholder("perm", permission).build());
            return "YES/NO?";
        }
    };
}
