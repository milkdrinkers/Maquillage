package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.gui.GuiHandler;

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
                CommandEdit.registerCommandEdit()
            )
            .register();
    }

}
