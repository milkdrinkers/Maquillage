package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.maquillage.gui.NameColorGui;

public class CommandNamecolor {
    public static CommandAPICommand registerCommandNamecolor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.command.set.color")
            .executesPlayer((sender, args) -> {
                new NameColorGui().open(sender);
            });
    }
}
