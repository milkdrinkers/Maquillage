package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import io.github.milkdrinkers.maquillage.utility.conversation.Conversation;
import io.github.milkdrinkers.maquillage.utility.conversation.color.DeleteColorConversation;
import io.github.milkdrinkers.maquillage.utility.conversation.tag.DeleteTagConversation;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandDelete {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversation.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandDelete(boolean tags, boolean colors) {
        CommandAPICommand commandDelete = new CommandAPICommand("delete")
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
                new StringArgument("key")
                    .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> NameColorHolder.getInstance().getAllKeys()))
            )
            .executesPlayer((sender, args) -> {
                String identifier = args.get("key").toString();
                if (!NameColorHolder.getInstance().doesKeyExist(identifier))
                    throw CommandAPIPaper.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.namecolor.delete.no-color")).build());
                factory.withFirstPrompt(DeleteColorConversation.confirmDeletePrompt(NameColorHolder.getInstance().getByKey(identifier)));

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

    private static CommandAPICommand registerSubcommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.admin.delete.tag")
            .withArguments(
                new StringArgument("key")
                    .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> TagHolder.getInstance().getAllKeys()))
            )
            .executesPlayer((sender, args) -> {
                String identifier = args.get("key").toString();
                if (!TagHolder.getInstance().doesKeyExist(identifier))
                    throw CommandAPIPaper.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.tag.delete.no-tag")).build());
                factory.withFirstPrompt(DeleteTagConversation.confirmDeletePrompt(TagHolder.getInstance().getByKey(identifier)));

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

}
