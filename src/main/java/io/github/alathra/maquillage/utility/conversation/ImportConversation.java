package io.github.alathra.maquillage.utility.conversation;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.alathra.maquillage.utility.ImportUtil;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImportConversation {

    public static Prompt confirmPrompt = new FixedSetPrompt("YES", "NO", "yes", "no", "Yes", "No") {
        @Override
        protected @Nullable Prompt acceptValidatedInput(@NotNull ConversationContext conversationContext, @NotNull String s) {
            Player player = (Player) conversationContext.getForWhom();
            if (s.equalsIgnoreCase("YES")) {
                ImportUtil.getInstance().addAllTags();
                ImportUtil.getInstance().addAllNamecolors();
                player.sendMessage(ColorParser.of("<green>Imported " + ImportUtil.getInstance().getTagAndNamecolorAmounts() + ".").build());
                return Prompt.END_OF_CONVERSATION;
            }
            player.sendMessage(ColorParser.of("<red>Nothing was imported.").build());
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext conversationContext) {
            Player player = (Player) conversationContext.getForWhom();
            player.sendMessage(ColorParser.of("Are you sure you want to import " + ImportUtil.getInstance().getTagAndNamecolorAmounts() + "?").build());
            return "YES/NO";
        }
    };


}
