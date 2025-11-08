package io.github.milkdrinkers.maquillage.utility.conversation.color;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.wordweaver.Translation;
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
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.color.current-color"))
                .with("namecolor", color.getColor() + player.getName()).build());
            return Translation.of("commands.module.namecolor.edit.color.current-color-question");
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
                boolean success = NameColorHolder.getInstance().update(updatedColor, color.getPerm(), color.getLabel(), color.getDatabaseId(), 0);
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.color.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.color.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.color.not-updated")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.color.confirm"))
                .with("namecolor", updatedColor + player.getName()).build());
            return "YES/NO";
        }
    };
}
