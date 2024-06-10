package io.github.Alathra.Maquillage.command;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

/**
 * A class to handle registration of commands.
 */
public class CommandHandler implements Reloadable {
    private final Maquillage plugin;

    public CommandHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).shouldHookPaperReload(true).silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();

        // Register commands here
        new CommandCreate();
        new CommandMaquillage();
        new CommandEdit();
        new CommandDelete();
        CommandName.registerCommandName().withAliases("namecolor").register();
        CommandTag.registerCommandTag().withAliases("tags").register();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}