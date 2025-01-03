package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.maquillage.gui.GuiHandler;

public class CommandNamecolor {
    public static CommandAPICommand registerCommandNamecolor() {
        return new CommandAPICommand("color")
            .withPermission("maquillage.command.set.color")
            .executesPlayer((sender, args) -> {
                PaginatedGui gui = GuiHandler.buildGui(GuiHandler.MaquillageGuiType.COLOR);
                GuiHandler.populateGui(GuiHandler.MaquillageGuiType.COLOR, gui, sender);
                gui.open(sender);
            });
    }
}
