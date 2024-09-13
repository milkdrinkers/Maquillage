package io.github.Alathra.Maquillage.command;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.color.DeleteColorConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.DeleteTagConversation;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandDelete {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandDelete() {
        return new CommandAPICommand("delete")
            .withPermission("maquillage.delete")
            .withShortDescription("Deletes a Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withPermission("maquillage.delete.color")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    NameColorHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        String identifier = args.get("identifier").toString();
                        if (!NameColorHolder.getInstance().doesIdentifierExist(identifier))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>There's no color with that identifier.").build());
                        factory.withFirstPrompt(DeleteColorConversation.confirmDeletePrompt(NameColorHolder.getInstance().getByIDString(identifier)));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("tag")
                    .withPermission("maquillage.delete.tag")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    TagHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        String identifier = args.get("identifier").toString();
                        if (!TagHolder.getInstance().doesIdentifierExist(identifier))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>There's no tag with that identifier").build());
                        factory.withFirstPrompt(DeleteTagConversation.confirmDeletePrompt(TagHolder.getInstance().getByIDString(identifier)));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    })
            );
    }

}
