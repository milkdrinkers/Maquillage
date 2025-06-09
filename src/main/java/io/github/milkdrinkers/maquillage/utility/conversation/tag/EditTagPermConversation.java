package io.github.milkdrinkers.maquillage.utility.conversation.tag;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.Tag;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditTagPermConversation {

    static String currentPerm;
    static String updatedPerm;
    static Tag tag;

    public static Prompt editPermPrompt(Tag tag) {
        EditTagPermConversation.tag = tag;
        EditTagPermConversation.currentPerm = tag.getPerm();
        return editPermStringPrompt;
    }

    static Prompt editPermStringPrompt = new StringPrompt() {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();

            if (currentPerm.isEmpty()) {
                player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.current-no-perm"))
                    .with("tag", tag.getTag()).build());
            } else {
                player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.current-perm"))
                    .with("tag", tag.getTag())
                    .with("perm", currentPerm).build());
            }


            return Translation.of("commands.module.tag.edit.perm.current-perm-question");
        }

        @Override
        public @Nullable Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
            if (s.toLowerCase().equals("none")){
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
                boolean success = TagHolder.getInstance().update(tag.getTag(), updatedPerm, tag.getLabel(), tag.getDatabaseId());
                if (success) {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.success")).build());
                } else {
                    player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.failure")).build());
                }
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.not-updated")).build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of(Translation.of("commands.module.tag.edit.perm.confirm"))
                .with("tag", tag.getTag())
                .with("perm", updatedPerm).build());
            return "YES/NO?";
        }
    };
}
