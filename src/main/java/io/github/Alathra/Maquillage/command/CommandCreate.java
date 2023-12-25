package io.github.Alathra.Maquillage.command;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.utility.Cfg;
import io.github.Alathra.Maquillage.utility.ColorConversation;
import io.github.Alathra.Maquillage.utility.Conversations;
import io.github.Alathra.Maquillage.utility.TagConversation;
import org.bukkit.conversations.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandCreate {

    Plugin plugin = Maquillage.getInstance();
    ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    // TODO: save tags and colors to DB
    public CommandCreate() {
        new CommandAPICommand("maquillagecreate")
            .withAliases("mcreate", "maqcreate")
            .withShortDescription("Creates a new Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withAliases("colour")
                    .withPermission("maquillage.create.color")
                    .executes((sender, args) -> {
                        factory.withFirstPrompt(ColorConversation.newColorPrompt);

                        Conversation conversation = factory.buildConversation((Conversable) sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("tag")
                    .withAliases("prefix")
                    .withPermission("maquillage.create.tag")
                    .executes((sender, args) -> {
                        factory.withFirstPrompt(TagConversation.newTagPrompt);

                        Conversation conversation = factory.buildConversation((Conversable) sender);
                        conversation.begin();
                    })
            )
            .register();
    }

}
