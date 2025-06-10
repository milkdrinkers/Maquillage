package io.github.milkdrinkers.maquillage.command;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import io.github.milkdrinkers.wordweaver.Translation;
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
                new StringArgument("key").replaceSuggestions(ArgumentSuggestions.stringCollection(unused -> Translation.getKeys()))
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
        Translation.setLanguage(Cfg.get().get("language", "en_US"));
        Translation.reload();
        sender.sendMessage(Translation.as("commands.translation.reloaded"));
    }

    private static void executorTest(CommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException {
        if (!(args.getOrDefault("key", "") instanceof final String key))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>A translation key must be a string!").build());

        if (key.isBlank())
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>A translation key cannot be empty!").build());

        if (key.startsWith(".") || key.endsWith("."))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>A translation key cannot begin/end with a period!").build());

        final String translation = Translation.of(key);

        if (translation == null)
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That translation entry doesn't exist!").build());

        if (translation.isBlank())
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That translation entry is an empty string!").build());

        sender.sendMessage(Translation.as(key));
    }
}
