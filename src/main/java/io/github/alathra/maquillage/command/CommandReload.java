package io.github.alathra.maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.alathra.maquillage.Maquillage;

class CommandReload {
    public static CommandAPICommand registerCommandReload() {
        return new CommandAPICommand("reload")
            .withPermission("maquillage.command.admin.reload")
            .executesPlayer((sender, args) -> {
                Maquillage.getInstance().onReload();
            });
    }
}
