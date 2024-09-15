package io.github.alathra.maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.alathra.maquillage.gui.GuiHandler;

class CommandName {
    public static CommandAPICommand registerCommandName() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.set.color")
            .executesPlayer((sender, args) -> {
                PaginatedGui gui = GuiHandler.buildGui(GuiHandler.MaquillageGuiType.COLOR);
                GuiHandler.populateGui(GuiHandler.MaquillageGuiType.COLOR, gui, sender);
                gui.open(sender);
            });
    }
}
