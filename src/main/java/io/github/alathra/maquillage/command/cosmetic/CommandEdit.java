package io.github.alathra.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import io.github.alathra.maquillage.utility.conversation.Conversation;
import io.github.alathra.maquillage.utility.conversation.color.EditColorColorConversation;
import io.github.alathra.maquillage.utility.conversation.color.EditColorLabelConversation;
import io.github.alathra.maquillage.utility.conversation.color.EditColorPermConversation;
import io.github.alathra.maquillage.utility.conversation.tag.EditTagLabelConversation;
import io.github.alathra.maquillage.utility.conversation.tag.EditTagPermConversation;
import io.github.alathra.maquillage.utility.conversation.tag.EditTagTagConversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandEdit {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversation.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandEdit(boolean tags, boolean colors) {
        CommandAPICommand commandEdit =  new CommandAPICommand("edit")
            .withPermission("maquillage.command.admin.edit")
            .withShortDescription("Edits a Maquillage color or tag.");

        if (tags)
            commandEdit.withSubcommand(registerSubcommandTag());

        if (colors)
            commandEdit.withSubcommand(registerSubcommandColor());

        return commandEdit;
    }

    private static CommandAPICommand registerSubcommandColor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.command.admin.edit.color")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withArguments(
                        new StringArgument("key")
                            .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> NameColorHolder.getInstance().getAllKeys()))
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditColorColorConversation.editColorPrompt(NameColorHolder.getInstance().getByKey(args.get("key").toString())));

                        org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("label")
                    .withArguments(
                        new StringArgument("key")
                            .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> NameColorHolder.getInstance().getAllKeys()))
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditColorLabelConversation.editNamePrompt(NameColorHolder.getInstance().getByKey(args.get("key").toString())));

                        org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("permission")
                    .withArguments(
                        new StringArgument("key")
                            .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> NameColorHolder.getInstance().getAllKeys()))
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditColorPermConversation.editPermPrompt(NameColorHolder.getInstance().getByKey(args.get("key").toString())));

                        org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    })
            );
    }

    private static CommandAPICommand registerSubcommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.admin.edit.tag")
            .withSubcommands(
                new CommandAPICommand("tag")
                    .withArguments(
                        new StringArgument("key")
                            .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> TagHolder.getInstance().getAllKeys()))
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditTagTagConversation.editTagPrompt(TagHolder.getInstance().getByKey(args.get("key").toString())));

                        org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("label")
                    .withArguments(
                        new StringArgument("key")
                            .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> TagHolder.getInstance().getAllKeys()))
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditTagLabelConversation.editNamePrompt(TagHolder.getInstance().getByKey(args.get("key").toString())));

                        org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("permission")
                    .withArguments(
                        new StringArgument("key")
                            .replaceSuggestions(ArgumentSuggestions.stringCollection(info -> TagHolder.getInstance().getAllKeys()))
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditTagPermConversation.editPermPrompt(TagHolder.getInstance().getByKey(args.get("key").toString())));

                        org.bukkit.conversations.Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    })
            );
    }

}
