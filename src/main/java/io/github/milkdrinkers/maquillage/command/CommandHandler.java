package io.github.milkdrinkers.maquillage.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.Reloadable;
import io.github.milkdrinkers.maquillage.command.cosmetic.CommandNamecolor;
import io.github.milkdrinkers.maquillage.command.cosmetic.CommandTag;
import io.github.milkdrinkers.maquillage.command.nickname.CommandNickname;
import io.github.milkdrinkers.maquillage.command.nickname.CommandRealname;
import io.github.milkdrinkers.maquillage.utility.Cfg;

/**
 * A class to handle registration of commands.
 */
public class CommandHandler implements Reloadable {
    public static final String BASE_PERM = "maquillage.command";
    private final Maquillage plugin;

    public CommandHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(Maquillage plugin) {
        CommandAPI.onLoad(
            new CommandAPIPaperConfig(plugin)
                .silentLogs(true)
        );
    }

    @Override
    public void onEnable(Maquillage plugin) {
        if (!CommandAPI.isLoaded())
            return;

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
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onDisable();
    }
}