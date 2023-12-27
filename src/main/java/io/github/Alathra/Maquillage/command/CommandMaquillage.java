package io.github.Alathra.Maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.Alathra.Maquillage.gui.GUIHandler;
import io.github.Alathra.Maquillage.gui.PopulateBorder;
import io.github.Alathra.Maquillage.gui.PopulateButtons;
import net.kyori.adventure.text.Component;

public class CommandMaquillage {
    // TODO: logic
    public CommandMaquillage() {
        new CommandAPICommand("maquillage")
            .withAliases("maq")
            .withShortDescription("The main command for the plugin Maquillage.")
            .withSubcommands(
                new CommandAPICommand("tag")
                    .withAliases("prefix")
                    .withPermission("maquillage.set.tag")
                    .executesPlayer((sender, args) -> {
                        PaginatedGui gui = Gui.paginated()
                            .title(Component.text("Test"))
                            .rows(6)
                            .disableItemPlace()
                            .disableItemSwap()
                            .disableItemDrop()
                            .disableItemTake()
                            .create();

                        PopulateBorder.populateBorder(gui);
                        PopulateButtons.populateButtons(gui, sender, GUIHandler.MaquillageGuiType.TAG);

                        gui.open(sender);
                    }),
                new CommandAPICommand("color")
                    .withAliases("colour")
                    .withPermission("maquillage.set.color")
                    .executesPlayer((sender, args) -> {

                    }),
                new CommandAPICommand("admin")
                    .withPermission("maquillage.admin")
                    .executesPlayer((sender, args) -> {

                    })
            )
            .register();
    }

}
