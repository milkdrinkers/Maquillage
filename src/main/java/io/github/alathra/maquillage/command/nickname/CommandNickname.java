package io.github.alathra.maquillage.command.nickname;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

import java.util.List;

public class CommandNickname {

    public static CommandAPICommand registerCommandNickname() {
        return new CommandAPICommand("nickname")
            .withAliases("nick")
            .withPermission("maquillage.nick")
            .withSubcommands(
                new CommandAPICommand("set")
                    .withPermission("maquillage.nick.set")
                    .withArguments(List.of(
                        new PlayerArgument("player"),
                        new StringArgument("nick")))
                    .executes((sender, args) -> {
                        // TODO: do stuff :)
                    }),
                new CommandAPICommand("clear")
                    .withPermission("maquillage.nick.clear")
                    .withArguments(new PlayerArgument("player"))
                    .executes((sender, args) -> {

                    })
            );
    }

}
