package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.utility.conversation.Conversation;
import io.github.milkdrinkers.maquillage.utility.conversation.color.ColorConversation;
import io.github.milkdrinkers.maquillage.utility.conversation.tag.TagConversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandCreate {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversation.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandCreate(boolean tags, boolean colors) {
        CommandAPICommand commandCreate = new CommandAPICommand("create")
            .withPermission("maquillage.command.admin.create")
            .withShortDescription("Creates a new Maquillage color or tag.");

        if (tags)
            commandCreate.withSubcommand(registerSubcommandTag());

        if (colors)
            commandCreate.withSubcommand(registerSubcommandColor());

        return commandCreate;
    }

    private static CommandAPICommand registerSubcommandColor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.command.admin.create.color")
            .executesPlayer((sender, args) -> {
                factory.withFirstPrompt(ColorConversation.newColorPrompt);

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

    private static CommandAPICommand registerSubcommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.admin.create.tag")
            .executesPlayer((sender, args) -> {
                factory.withFirstPrompt(TagConversation.newTagPrompt);

                org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                conversation.begin();
            });
    }

}
