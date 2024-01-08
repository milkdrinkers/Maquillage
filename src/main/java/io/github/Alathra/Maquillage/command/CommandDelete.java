package io.github.Alathra.Maquillage.command;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.color.DeleteColorConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.DeleteTagConversation;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandDelete {

    Plugin plugin = Maquillage.getInstance();
    ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public CommandDelete() {
        new CommandAPICommand("maquillagedelete")
            .withAliases("mdelete", "maqdelete")
            .withPermission("maquillage.delete")
            .withShortDescription("Deletes a Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withPermission("maquillage.delete.color")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    NameColorHandler.getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        String identifier = args.get("identifier").toString();
                        if (!NameColorHandler.doesIdentifierExist(identifier))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>There's no color with that identifier.").build());
                        factory.withFirstPrompt(DeleteColorConversation.confirmDeletePrompt(NameColorHandler.getNameColorByIDString(identifier)));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("tag")
                    .withPermission("maquillage.delete.tag")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    TagHandler.getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        String identifier = args.get("identifier").toString();
                        if(!TagHandler.doesIdentifierExist(identifier))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>There's no tag with that identifier").build());
                        factory.withFirstPrompt(DeleteTagConversation.confirmDeletePrompt(TagHandler.getTagByIDString(identifier)));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    })
            )
            .register();
    }

}
