package io.github.alathra.maquillage.command.nickname;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

import java.util.List;

public class CommandRealname {

    public static CommandAPICommand registerCommandRealname() {
        return new CommandAPICommand("realname")
            .withPermission("maquillage.realname")
            .withArguments(List.of(
                new PlayerArgument("player")
            .executes((sender, args) -> {
                // TODO: do stuff :)
            })));
    }


}
