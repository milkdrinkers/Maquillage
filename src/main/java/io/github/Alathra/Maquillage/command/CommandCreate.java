package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandCreate {
    // TODO: logic
    public CommandCreate() {
        new CommandAPICommand("maquillagecreate")
            .withAliases("mcreate", "maqcreate")
            .withShortDescription("Creates a new Maquillage color or tag.")
            .withSubcommands(
                new CommandAPICommand("color")
                    .withAliases("colour")
                    .withPermission("maquillage.create.color")
                    .executes((sender, args) -> {

                    }),
                new CommandAPICommand("tag")
                    .withAliases("prefix")
                    .withPermission("maquillage.create.tag")
                    .executes((sender, args) -> {

                    })
            )
            .register();
    }

}
