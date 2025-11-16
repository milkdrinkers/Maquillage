package io.github.milkdrinkers.maquillage.command;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.maquillage.command.cosmetic.*;

class CommandMaquillage {
    public static CommandAPICommand registerCommandMaquillage(boolean tags, boolean colors) {
        CommandAPICommand commandMaquillage = new CommandAPICommand("maquillage")
            .withAliases("maq")
            .withShortDescription("The main command for the plugin Maquillage.")
            .withSubcommands(
                new TranslationCommand().command(),
                new DumpCommand().command(),
                CommandReload.registerCommandReload(),
                CommandEdit.registerCommandEdit(tags, colors),
                CommandImport.registerCommandImport()
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
