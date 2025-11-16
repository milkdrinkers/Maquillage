package io.github.milkdrinkers.maquillage.command.cosmetic;

import dev.jorel.commandapi.CommandAPICommand;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.utility.ImportUtil;

public class CommandImport {

    static Plugin plugin = Maquillage.getInstance();
    static ConversationFactory factory = new ConversationFactory(plugin).withPrefix(Conversation.prefix).withLocalEcho(false);

    public static CommandAPICommand registerCommandImport() {
        return new CommandAPICommand("import")
            .withPermission("maquillage.import")
            .withShortDescription("Imports cosmetics from the import.yml file.")
            .withSubcommands(
                registerCommandImportSupreme(),
                registerCommandImportAlonso())
            .executesPlayer((sender, args) -> {
                ImportUtil.addAllTags();
                ImportUtil.addAllNamecolors();
                sender.sendMessage(ColorParser.of("<green>Attempted to import " + ImportUtil.getTagAndNamecolorAmounts() + ".").build());
            });
    }
}
