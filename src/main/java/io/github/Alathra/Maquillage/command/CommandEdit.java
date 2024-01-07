package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorColorConversation;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorNameConversation;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorPermConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.EditTagNameConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.EditTagPermConversation;
import io.github.Alathra.Maquillage.utility.conversations.tag.EditTagTagConversation;
import org.bukkit.Bukkit;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

public class CommandEdit {

    Plugin plugin = Maquillage.getInstance();
    ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversations.prefix).withLocalEcho(false);

    public CommandEdit() {
        new CommandAPICommand("maquillageedit")
            .withAliases("medit", "maqedit")
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
                                            NameColorHandler.getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditColorColorConversation.editColorPrompt(NameColorHandler.getNameColorByIDString(args.get("identifier").toString())));

                                Conversation conversation = factory.buildConversation(sender);
                                conversation.begin();
                            }),
                        new CommandAPICommand("name")
                            .withArguments(
                                new StringArgument("identifier")
                                    .replaceSuggestions(
                                        ArgumentSuggestions.strings(
                                            NameColorHandler.getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditColorNameConversation.editNamePrompt(NameColorHandler.getNameColorByIDString(args.get("identifier").toString())));

                                Conversation conversation = factory.buildConversation(sender);
                                conversation.begin();
                            }),
                        new CommandAPICommand("permission")
                            .withArguments(
                                new StringArgument("identifier")
                                    .replaceSuggestions(
                                        ArgumentSuggestions.strings(
                                            NameColorHandler.getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditColorPermConversation.editPermPrompt(NameColorHandler.getNameColorByIDString(args.get("identifier").toString())));

                                Conversation conversation = factory.buildConversation(sender);
                                conversation.begin();
                            }),
                        new CommandAPICommand("keys")
                            .executesPlayer((sender, args) -> {
                                sender.sendMessage(NameColorHandler.getAllIdentifiers().toString());
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
                                            TagHandler.getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditTagTagConversation.editTagPrompt(TagHandler.getTagByIDString(args.get("identifier").toString())));

                                Conversation conversation = factory.buildConversation(sender);
                                conversation.begin();
                            }),
                        new CommandAPICommand("name")
                            .withArguments(
                                new StringArgument("identifier")
                                    .replaceSuggestions(
                                        ArgumentSuggestions.strings(
                                            TagHandler.getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditTagNameConversation.editNamePrompt(TagHandler.getTagByIDString(args.get("identifier").toString())));

                                Conversation conversation = factory.buildConversation(sender);
                                conversation.begin();
                            }),
                        new CommandAPICommand("permission")
                            .withArguments(
                                new StringArgument("identifier")
                                    .replaceSuggestions(
                                        ArgumentSuggestions.strings(
                                            TagHandler.getAllIdentifiers()
                                        )
                                    )
                            )
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditTagPermConversation.editPermPrompt(TagHandler.getTagByIDString(args.get("identifier").toString())));

                                Conversation conversation = factory.buildConversation(sender);
                                conversation.begin();
                            }),
                        new CommandAPICommand("keys")
                            .executesPlayer((sender, args) -> {
                                sender.sendMessage(TagHandler.getAllIdentifiers().toString());
                            })
                    )
            )
            .register();
    }

}
