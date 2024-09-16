package io.github.alathra.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.cosmetic.tag.TagHolder;
import io.github.alathra.maquillage.utility.conversations.Conversations;
import io.github.alathra.maquillage.utility.conversations.color.EditColorColorConversation;
import io.github.alathra.maquillage.utility.conversations.color.EditColorLabelConversation;
import io.github.alathra.maquillage.utility.conversations.color.EditColorPermConversation;
import io.github.alathra.maquillage.utility.conversations.tag.EditTagLabelConversation;
import io.github.alathra.maquillage.utility.conversations.tag.EditTagPermConversation;
import io.github.alathra.maquillage.utility.conversations.tag.EditTagTagConversation;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandEdit {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandEdit(boolean tags, boolean colors) {
        CommandAPICommand commandEdit =  new CommandAPICommand("edit")
            .withPermission("maquillage.edit")
            .withShortDescription("Edits a Maquillage color or tag.");

        if (tags)
            commandEdit.withSubcommand(registerSubcommandTag());

        if (colors)
            commandEdit.withSubcommand(registerSubcommandColor());

        return commandEdit;
    }

    private static CommandAPICommand registerSubcommandColor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.edit.color")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    NameColorHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditColorColorConversation.editColorPrompt(NameColorHolder.getInstance().getByIDString(args.get("identifier").toString())));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("label")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    NameColorHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditColorLabelConversation.editNamePrompt(NameColorHolder.getInstance().getByIDString(args.get("identifier").toString())));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("permission")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    NameColorHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditColorPermConversation.editPermPrompt(NameColorHolder.getInstance().getByIDString(args.get("identifier").toString())));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("keys")
                    .executesPlayer((sender, args) -> {
                        sender.sendMessage(NameColorHolder.getInstance().getAllIdentifiers().toString());
                    })
            );
    }

    private static CommandAPICommand registerSubcommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.edit.tag")
            .withSubcommands(
                new CommandAPICommand("tag")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    TagHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditTagTagConversation.editTagPrompt(TagHolder.getInstance().getByIDString(args.get("identifier").toString())));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("label")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    TagHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditTagLabelConversation.editNamePrompt(TagHolder.getInstance().getByIDString(args.get("identifier").toString())));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("permission")
                    .withArguments(
                        new StringArgument("identifier")
                            .replaceSuggestions(
                                ArgumentSuggestions.strings(
                                    TagHolder.getInstance().getAllIdentifiers()
                                )
                            )
                    )
                    .executesPlayer((sender, args) -> {
                        factory.withFirstPrompt(EditTagPermConversation.editPermPrompt(TagHolder.getInstance().getByIDString(args.get("identifier").toString())));

                        Conversation conversation = factory.buildConversation(sender);
                        conversation.begin();
                    }),
                new CommandAPICommand("keys")
                    .executesPlayer((sender, args) -> {
                        sender.sendMessage(TagHolder.getInstance().getAllIdentifiers().toString());
                    })
            );
    }

}
