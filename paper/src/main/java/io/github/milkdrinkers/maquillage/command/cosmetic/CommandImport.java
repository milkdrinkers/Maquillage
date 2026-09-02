package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.utility.ImportUtil;

public class CommandImport {
    public static CommandAPICommand registerCommandImport() {
        return new CommandAPICommand("import")
            .withPermission("maquillage.import")
            .withShortDescription("Imports cosmetics from the import.yml file.")
            .withSubcommands()
            .executes((sender, args) -> {
                ImportUtil.importTags();
                ImportUtil.importColors();
                sender.sendMessage(ColorParser.of("<green>Attempted to import " + ImportUtil.getTagAndNamecolorAmounts() + ".").build());
            });
    }
}
