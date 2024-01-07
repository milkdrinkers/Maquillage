package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.utility.conversations.Conversations;
import io.github.Alathra.Maquillage.utility.conversations.color.EditColorColorConversation;
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
                            .executesPlayer((sender, args) -> {
                                factory.withFirstPrompt(EditColorColorConversation.editColorPrompt(NameColorHandler.getNameColorByIDString(args.get(0).toString())));
                            }),
                        new CommandAPICommand("name")
                            .executesPlayer((sender, args) -> {

                            }),
                        new CommandAPICommand("permission")
                            .executesPlayer((sender, args) -> {

                            })
                    ),
                new CommandAPICommand("tag")
                    .withPermission("maquillage.edit.tag")
                    .withSubcommands(
                        new CommandAPICommand("tag")
                            .executesPlayer((sender, args) -> {

                            }),
                        new CommandAPICommand("name")
                            .executesPlayer((sender, args) -> {

                            }),
                        new CommandAPICommand("permission")
                            .executesPlayer((sender, args) -> {

                            })
                    )
            )
            .register();
    }

}
