package io.github.Alathra.Maquillage.command.cosmetics;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorColorConversation;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorNameConversation;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorPermConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.EditTagNameConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.EditTagPermConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.EditTagTagConversation;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandEdit {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandEdit() {
        return new CommandAPICommand("edit")
            .withPermission("maquillage.edit")
            .withShortDescription("Edits a Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
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
                        new CommandAPICommand("name")
                            .withArguments(
                                new StringArgument("identifier")
                                    .replaceSuggestions(
                                        ArgumentSuggestions.strings(
                                            NameColorHolder.getInstance().getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditColorNameConversation.editNamePrompt(NameColorHolder.getInstance().getByIDString(args.get("identifier").toString())));

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
                    ),
                new CommandAPICommand("tag")
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
                        new CommandAPICommand("name")
                            .withArguments(
                                new StringArgument("identifier")
                                    .replaceSuggestions(
                                        ArgumentSuggestions.strings(
                                            TagHolder.getInstance().getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditTagNameConversation.editNamePrompt(TagHolder.getInstance().getByIDString(args.get("identifier").toString())));

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
                    )
            );
    }

}
