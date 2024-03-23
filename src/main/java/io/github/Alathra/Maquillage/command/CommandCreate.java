package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.utility.conversations.color.ColorConversation;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.tag.TagConversation;
import org.bukkit.conversations.*;
import org.bukkit.plugin.Plugin;

public class CommandCreate {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandCreate() {
        return new CommandAPICommand("create")
            .withPermission("maquillage.create")
            .withShortDescription("Creates a new Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withPermission("maquillage.create.color")
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(ColorConversation.newColorPrompt);

                        Conversation conversation = factory.buildConversation((Conversable) sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("tag")
                    .withPermission("maquillage.create.tag")
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(TagConversation.newTagPrompt);

                        Conversation conversation = factory.buildConversation((Conversable) sender);
                        conversation.begin();
                    })
            );
    }

}
