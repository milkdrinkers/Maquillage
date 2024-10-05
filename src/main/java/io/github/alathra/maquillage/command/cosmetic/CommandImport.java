package io.github.alathra.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.utility.conversation.Conversation;
import io.github.alathra.maquillage.utility.conversation.ImportConversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandImport {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversation.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandImport() {
        return new CommandAPICommand("import")
            .withPermission("maquillage.import")
            .withShortDescription("Imports cosmetics from the import.yml file.")
            .executesPlayer((sender, args) -> {
                factory.withFirstPrompt(ImportConversation.confirmPrompt);

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

}
