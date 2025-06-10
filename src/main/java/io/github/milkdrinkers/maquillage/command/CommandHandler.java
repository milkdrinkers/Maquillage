package io.github.milkdrinkers.maquillage.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.command.cosmetic.*;
import io.github.milkdrinkers.maquillage.command.nickname.CommandNickname;
import io.github.milkdrinkers.maquillage.command.nickname.CommandRealname;
import io.github.milkdrinkers.maquillage.utility.Cfg;

/**
 * A class to handle registration of commands.
 */
public class CommandHandler implements Reloadable {
    private final Maquillage plugin;

    public CommandHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(Maquillage plugin) {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).shouldHookPaperReload(true).silentLogs(true));
    }

    @Override
    public void onEnable(Maquillage plugin) {
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

        if (nicks) {
            CommandNickname.registerCommandNickname("nickname", "nick").register();
            CommandNickname.registerSet("setnick", "setnickname").register();
            CommandNickname.registerClear("unnick", "clearnick", "clearnickname").register();
            CommandRealname.registerCommandRealname("realname").register();
        }
    }

    @Override
    public void onDisable(Maquillage plugin) {
        CommandAPI.onDisable();
    }
}