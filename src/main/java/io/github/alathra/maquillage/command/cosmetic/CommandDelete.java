package io.github.alathra.maquillage.command.cosmetic;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import io.github.alathra.maquillage.utility.conversation.Conversation;
import io.github.alathra.maquillage.utility.conversation.color.DeleteColorConversation;
import io.github.alathra.maquillage.utility.conversation.tag.DeleteTagConversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandDelete {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversation.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandDelete(boolean tags, boolean colors) {
        CommandAPICommand commandDelete =  new CommandAPICommand("delete")
            .withPermission("maquillage.command.admin.delete")
            .withShortDescription("Deletes a Maquillage color or tag.");

        if (tags)
            commandDelete.withSubcommand(registerSubcommandTag());

        if (colors)
            commandDelete.withSubcommand(registerSubcommandColor());

        return commandDelete;
    }

    private static CommandAPICommand registerSubcommandColor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.command.admin.delete.color")
            .withArguments(
                new StringArgument("identifier")
                    .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> NameColorHolder.getInstance().getAllKeys()))
            )
            .executesPlayer((sender, args) -> {
                String identifier = args.get("identifier").toString();
                if (!NameColorHolder.getInstance().doesKeyExist(identifier))
                    throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>There's no color with that identifier.").build());
                factory.withFirstPrompt(DeleteColorConversation.confirmDeletePrompt(NameColorHolder.getInstance().getByKey(identifier)));

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

    private static CommandAPICommand registerSubcommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.admin.delete.tag")
            .withArguments(
                new StringArgument("identifier")
                    .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> TagHolder.getInstance().getAllKeys()))
            )
            .executesPlayer((sender, args) -> {
                String identifier = args.get("identifier").toString();
                if (!TagHolder.getInstance().doesKeyExist(identifier))
                    throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>There's no tag with that identifier").build());
                factory.withFirstPrompt(DeleteTagConversation.confirmDeletePrompt(TagHolder.getInstance().getByKey(identifier)));

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

}
