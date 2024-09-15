package io.github.alathra.maquillage.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.Reloadable;
import io.github.alathra.maquillage.command.cosmetic.*;
import io.github.alathra.maquillage.utility.Cfg;

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

        boolean tags = Cfg.get().getBoolean("module.tag.enabled");
        boolean colors = Cfg.get().getBoolean("module.namecolor.enabled");
        boolean nicks = Cfg.get().getBoolean("module.nickname.enabled");

        // Register commands here
        CommandMaquillage.registerCommandMaquillage(tags, colors).register();

        if (tags)
            CommandTag.registerCommandTag().withAliases("tags").register();

        if (colors)
            CommandNamecolor.registerCommandNamecolor().withAliases("namecolor").register();
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}