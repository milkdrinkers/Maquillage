package io.github.alathra.maquillage.command.nickname;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.database.DatabaseQueries;
import io.github.alathra.maquillage.module.nickname.NicknameLookup;
import io.github.alathra.maquillage.player.PlayerData;
import io.github.alathra.maquillage.player.PlayerDataHolder;
import io.github.alathra.maquillage.translation.Translation;
import io.github.alathra.maquillage.utility.Cfg;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandNickname {

    public static CommandAPICommand registerCommandNickname() {
        return new CommandAPICommand("nickname")
            .withAliases("nick")
            .withPermission("maquillage.command.nick")
            .withSubcommands(
                new CommandAPICommand("set")
                    .withPermission("maquillage.command.nick.set")
                    .withArguments(List.of(
                        new PlayerArgument("player"),
                        new StringArgument("nick")))
                    .executes((sender, args) -> {
                        if (!(args.get("player") instanceof Player player))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>The player wasn't found.").build());

                        if (!Maquillage.getVaultHook().getPermissions().has(player, "maquillage.nick.admin")) {
                            if (NicknameCooldown.hasCooldown(player))
                                throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>This command has a cooldown. Try again in a few seconds.").build());
                        }

                        if (!(args.get("nick") instanceof String nick))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>You need to input a nickname.").build());

                        if(nick.length() > Cfg.get().getInt("module.nickname.length"))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That nickname is too long. The maximum length is " + Cfg.get().getInt("module.nickname.length") + " characters").build());

                        if (nick.matches("[^a-zA-Z0-9_ ]"))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That nickname contains illegal characters. Only A-Z, 0-9 and _ are allowed.").build());

                        PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);
                        data.setNickname(nick);
                        PlayerDataHolder.getInstance().setPlayerData(player, data);

                        NicknameLookup.getInstance().addNicknameToLookup(nick, player);

                        String prefix = "";
                        if (Cfg.get().getBoolean("module.nickname.prefix.enabled"))
                            prefix = Cfg.get().getString("module.nickname.prefix.string");

                        if (Cfg.get().getBoolean("module.nickname.set-displayname"))
                            player.displayName(Component.text(prefix + data.getNicknameString()));

                        if (Cfg.get().getBoolean("module.nickname.set-listname"))
                            player.playerListName(Component.text(prefix + data.getNicknameString()));

                        NicknameCooldown.setCooldown(player);

                        sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.success"))
                            .parseMinimessagePlaceholder("player", player.getName())
                            .parseMinimessagePlaceholder("nickname", nick).build());

                        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> {
                            DatabaseQueries.savePlayerNickname(player, nick);
                        });
                    }),
                new CommandAPICommand("clear")
                    .withPermission("maquillage.command.nick.clear")
                    .withArguments(new PlayerArgument("player"))
                    .executes((sender, args) -> {
                        if (!(args.get("player") instanceof Player player))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>The player wasn't found.").build());

                        PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);

                        if (data.getNickname().isEmpty())
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That player doesn't appear to have a nickname.").build());

                        NicknameLookup.getInstance().removeNicknameFromLookup(data.getNickname().get().getString());

                        data.clearNickname();
                        PlayerDataHolder.getInstance().setPlayerData(player, data);

                        if (Cfg.get().getBoolean("module.nickname.set-displayname"))
                            player.displayName(Component.text(player.getName()));

                        if (Cfg.get().getBoolean("module.nickname.set-listname"))
                            player.playerListName(Component.text(player.getName()));

                        sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.cleared"))
                            .parseMinimessagePlaceholder("player", player.getName()).build());

                        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> {
                            DatabaseQueries.clearPlayerNickname(player);
                        });
                    })
            );
    }

}
