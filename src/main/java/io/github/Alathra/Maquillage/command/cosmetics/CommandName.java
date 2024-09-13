package io.github.Alathra.Maquillage.command.cosmetics;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.gui.GuiHandler;

public class CommandName {
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
