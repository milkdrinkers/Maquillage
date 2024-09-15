package io.github.alathra.maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;

class CommandMaquillage {
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
                CommandReload.registerCommandReload(),
                CommandTranslation.registerCommand()
            )
            .register();
    }

}
