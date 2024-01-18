package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.gui.GuiHandler;
import io.github.Alathra.Maquillage.gui.PopulateBorder;
import io.github.Alathra.Maquillage.gui.PopulateButtons;
import io.github.Alathra.Maquillage.gui.PopulateContent;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import net.kyori.adventure.text.Component;

public class CommandMaquillage {
    public CommandMaquillage() {
        new CommandAPICommand("maquillage")
            .withAliases("maq")
            .withShortDescription("The main command for the plugin Maquillage.")
            .withSubcommands(
                new CommandAPICommand("tag")
                    .withPermission("maquillage.set.tag")
                    .executesPlayer((sender, args) -> {
                        PaginatedGui gui = GuiHandler.buildGui(GuiHandler.MaquillageGuiType.TAG);
                        GuiHandler.populateGui(GuiHandler.MaquillageGuiType.TAG, gui, sender);
                        gui.open(sender);
                    }),
                new CommandAPICommand("color")
                    .withPermission("maquillage.set.color")
                    .executesPlayer((sender, args) -> {
                        PaginatedGui gui = GuiHandler.buildGui(GuiHandler.MaquillageGuiType.COLOR);
                        GuiHandler.populateGui(GuiHandler.MaquillageGuiType.COLOR, gui, sender);
                        gui.open(sender);
                    })
            )
            .register();
    }

}
