package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.utility.conversations.ColorConversation;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.TagConversation;
import org.bukkit.conversations.*;
import org.bukkit.plugin.Plugin;

public class CommandCreate {

    Plugin plugin = Maquillage.getInstance();
    ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public CommandCreate() {
        new CommandAPICommand("maquillagecreate")
            .withAliases("mcreate", "maqcreate")
            .withPermission("maquillage.create")
            .withShortDescription("Creates a new Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withAliases("colour")
                    .withPermission("maquillage.create.color")
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(ColorConversation.newColorPrompt);

                        Conversation conversation = factory.buildConversation((Conversable) sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("tag")
                    .withAliases("prefix")
                    .withPermission("maquillage.create.tag")
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(TagConversation.newTagPrompt);

                        Conversation conversation = factory.buildConversation((Conversable) sender);
                        conversation.begin();
                    })
            )
            .register();
    }

}
