package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.milkdrinkers.maquillage.gui.GuiHandler;

public class CommandTag {
    public static CommandAPICommand registerCommandTag() {
        return new CommandAPICommand("tag")
            .withPermission("maquillage.command.set.tag")
            .executesPlayer((sender, args) -> {
                PaginatedGui gui = GuiHandler.buildGui(GuiHandler.MaquillageGuiType.TAG);
                GuiHandler.populateGui(GuiHandler.MaquillageGuiType.TAG, gui, sender);
                gui.open(sender);
            });
    }
}
