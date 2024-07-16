package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.Alathra.Maquillage.Maquillage;

public class CommandReload {
    public static CommandAPICommand registerCommandReload() {
        return new CommandAPICommand("reload")
            .withPermission("maquillage.reload")
            .executesPlayer((sender, args) -> {
                Maquillage.getInstance().onReload();
            });
    }
}
