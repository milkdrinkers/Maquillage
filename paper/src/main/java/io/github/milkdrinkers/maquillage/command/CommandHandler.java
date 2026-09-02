package io.github.milkdrinkers.maquillage.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import io.github.milkdrinkers.maquillage.AbstractMaquillage;
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
    public void onLoad(AbstractMaquillage plugin) {
        CommandAPI.onLoad(
            new CommandAPIPaperConfig(plugin)
                .silentLogs(true)
        );
    }

    @Override
    public void onEnable(AbstractMaquillage plugin) {
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onEnable();

        final boolean tags = Cfg.get().module.tag.enabled;
        final boolean colors = Cfg.get().module.namecolor.enabled;
        final boolean nicks = Cfg.get().module.nickname.enabled;

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
    public void onDisable(AbstractMaquillage plugin) {
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onDisable();
    }
}