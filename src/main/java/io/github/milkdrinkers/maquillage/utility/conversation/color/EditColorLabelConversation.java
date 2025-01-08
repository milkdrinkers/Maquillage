package io.github.milkdrinkers.maquillage.utility.conversation.color;

import io.github.milkdrinkers.colorparser.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColor;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditColorLabelConversation {

    static String currentLabel;
    static String updatedLabel;
    static NameColor color;

    public static Prompt editNamePrompt(NameColor color) {
        EditColorLabelConversation.color = color;
        EditColorLabelConversation.currentLabel = color.getLabel();
        return editNameStringPrompt;
    }

    static Prompt editNameStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.label.current-label"))
                .parseMinimessagePlaceholder("namecolor", color.getColor() + player.getName())
                .parseMinimessagePlaceholder("label", currentLabel).build());
            return Translation.of("commands.module.namecolor.edit.label.current-label-question");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            updatedLabel = s;
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = NameColorHolder.getInstance().update(color.getColor(), color.getPerm(), updatedLabel, color.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.label.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.label.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.label.not.updated")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.label.current-label"))
                .parseMinimessagePlaceholder("namecolor", color.getColor() + player.getName())
                .parseMinimessagePlaceholder("label", updatedLabel).build());
            return "YES/NO";
        }
    };
}
