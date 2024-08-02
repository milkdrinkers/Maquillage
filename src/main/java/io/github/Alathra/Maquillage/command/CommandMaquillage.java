package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandMaquillage {
    public CommandMaquillage() {
        new CommandAPICommand("maquillage")
            .withAliases("maq")
            .withShortDescription("The main command for the plugin Maquillage.")
            .withSubcommands(
                CommandTag.registerCommandTag(),
                CommandName.registerCommandName(),
                CommandCreate.registerCommandCreate(),
                CommandDelete.registerCommandDelete(),
                CommandEdit.registerCommandEdit(),
                CommandReload.registerCommandReload()
            )
            .register();
    }

}
