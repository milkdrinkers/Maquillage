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

public class EditColorPermConversation {

    static String currentPerm;
    static String updatedPerm;
    static NameColor color;

    public static Prompt editPermPrompt(NameColor color) {
        EditColorPermConversation.color = color;
        EditColorPermConversation.currentPerm = color.getPerm();
        return editPermStringPrompt;
    }

    static Prompt editPermStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();

            if (currentPerm.isEmpty()) {
                player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.current-no-perm"))
                    .with("namecolor", color.getColor() + player.getName()).build());
            } else {
                player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.current-perm"))
                    .with("namecolor", color.getColor() + player.getName())
                    .with("perm", currentPerm).build());
            }


            return Translation.of("commands.module.namecolor.edit.perm.current-perm-question");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            if (s.equalsIgnoreCase("none")) {
                updatedPerm = "";
            } else {
                while (s.charAt(0) == '.') {
                    s = s.substring(1);
                }
                updatedPerm = s;
            }
            return confirmPrompt;
        }
    };

    static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                boolean success = NameColorHolder.getInstance().update(color.getColor(), updatedPerm, color.getLabel(), color.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.not-updated")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.namecolor.edit.perm.confirm"))
                .with("perm", updatedPerm).build());
            return "YES/NO?";
        }
    };
}
