package io.github.alathra.maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.alathra.maquillage.command.cosmetic.*;

class CommandMaquillage {
    public static CommandAPICommand registerCommandMaquillage(boolean tags, boolean colors) {
        CommandAPICommand commandMaquillage = new CommandAPICommand("maquillage")
            .withAliases("maq")
            .withShortDescription("The main command for the plugin Maquillage.")
            .withSubcommands(
                CommandTranslation.registerCommandTranslation(),
                CommandReload.registerCommandReload(),
                CommandCreate.registerCommandCreate(tags, colors),
                CommandDelete.registerCommandDelete(tags, colors),
                CommandEdit.registerCommandEdit(tags, colors)
            );

         if (tags) {
            commandMaquillage.withSubcommands(CommandTag.registerCommandTag());
         }

         if (colors) {
            commandMaquillage.withSubcommands(CommandNamecolor.registerCommandNamecolor());
         }

         return commandMaquillage;
    }

}
