package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandMaquillage {
    // TODO: logic
    public CommandMaquillage() {
        new CommandAPICommand("maquillage")
            .withAliases("maq")
            .withShortDescription("The main command for the plugin Maquillage.")
            .withSubcommands(
                new CommandAPICommand("tag")
                    .withAliases("prefix")
                    .withPermission("maquillage.set.tag")
                    .executes((sender, args) -> {

                    }),
                new CommandAPICommand("color")
                    .withAliases("colour")
                    .withPermission("maquillage.set.color")
                    .executes((sender, args) -> {

                    }),
                new CommandAPICommand("admin")
                    .withPermission("maquillage.admin")
                    .executes((sender, args) -> {

                    })
            )
            .register();
    }

}
