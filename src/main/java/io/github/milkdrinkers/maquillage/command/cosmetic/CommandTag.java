package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.maquillage.gui.TagGui;

public class CommandTag {
    public static CommandAPICommand registerCommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.set.tag")
            .executesPlayer((sender, args) -> {
                new TagGui().open(sender);
            });
    }
}
