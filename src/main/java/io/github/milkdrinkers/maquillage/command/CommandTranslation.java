package io.github.milkdrinkers.maquillage.command;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.command.CommandSender;

/**
 * Class containing the code for the translation commands.
 */
class CommandTranslation {
    private static final String BASE_PERM = "maquillage.command.admin.translation";

    /**
     * Instantiates a new command tree.
     */
    protected static CommandAPICommand registerCommandTranslation() {
        return new CommandAPICommand("translation")
            .withFullDescription("Translation help command.")
            .withShortDescription("Translation help command.")
            .withPermission(BASE_PERM)
            .withSubcommands(
                commandReload(),
                commandTest(),
                new CommandAPICommand("help")
                    .executes(CommandTranslation::executorHelp)
            )
            .executes(CommandTranslation::executorHelp);
    }

    private static CommandAPICommand commandReload() {
        return new CommandAPICommand("reload")
            .withFullDescription("Reloads the translation files.")
            .withShortDescription("Reload the translation files.")
            .withPermission(BASE_PERM + ".reload")
            .executes(CommandTranslation::executorReload);
    }

    private static CommandAPICommand commandTest() {
        return new CommandAPICommand("test")
            .withFullDescription("Test a translation.")
            .withShortDescription("Test a translation.")
            .withPermission(BASE_PERM + ".test")
            .withArguments(
                new StringArgument("key").replaceSuggestions(ArgumentSuggestions.stringCollection(info -> Translation.getAllKeys()))
            )
            .executes(CommandTranslation::executorTest);
    }

    private static void executorHelp(CommandSender sender, CommandArguments args) {
        sender.sendMessage(
            ColorParser.of(Translation.of("commands.translation.help"))
                .legacy()
                .build()
        );
    }

    private static void executorReload(CommandSender sender, CommandArguments args) {
        Maquillage.getInstance().getTranslationManager().onReload();
        sender.sendMessage(
            ColorParser.of(Translation.of("commands.translation.reloaded"))
                .legacy()
                .build()
        );
    }

    private static void executorTest(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(args.getOrDefault("key", "") instanceof String key))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>You need to specify a valid translation string!").build());

        if (key.isEmpty() || key.startsWith(".") || Translation.of(key) == null || Translation.of(key).isEmpty())
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>This translation entry doesn't exist or is an empty string!").build());

        sender.sendMessage(
            ColorParser.of(Translation.of(key))
                .legacy()
                .build()
        );
    }
}
