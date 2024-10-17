package io.github.alathra.maquillage.command.cosmetic;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.utility.ImportUtil;
import io.github.alathra.maquillage.utility.conversation.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.Plugin;

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

    public static CommandAPICommand registerCommandImportSupreme() {
        return new CommandAPICommand("supremetags")
            .withPermission("maquillage.import")
            .withShortDescription("Imports cosmetics from the supreme-import.yml file.")
            .executesPlayer((sender, args) -> {
                ImportUtil.addSupremeTags();
                sender.sendMessage(ColorParser.of("<green>Attempted to import " + ImportUtil.getSupremeTagAmount() + ".").build());
            });
    }

    public static CommandAPICommand registerCommandImportAlonso() {
        return new CommandAPICommand("alonsotags")
            .withPermission("maquillage.import")
            .withShortDescription("Imports cosmetics from the alonso-import.yml file.")
            .executesPlayer((sender, args) -> {
                ImportUtil.addAlonsoTags();
                sender.sendMessage(ColorParser.of("<green>Attempted to import " + ImportUtil.getAlonsoTagAmount() + ".").build());
            });
    }
}
