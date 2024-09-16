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
import io.github.alathra.maquillage.utility.Cfg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandNickname {

    public static CommandAPICommand registerCommandNickname() {
        return new CommandAPICommand("nickname")
            .withAliases("nick")
            .withPermission("maquillage.nick")
            .withSubcommands(
                new CommandAPICommand("set")
                    .withPermission("maquillage.nick.set")
                    .withArguments(List.of(
                        new PlayerArgument("player"),
                        new StringArgument("nick")))
                    .executes((sender, args) -> {
                        Player player = (Player) args.get("player");

                        String nick = (String) args.get("nick");

                        if (player == null)
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>The player wasn't found.").build());

                        if (nick == null)
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>You need to input a nickname.").build());

                        if(nick.length() > Cfg.get().getInt("module.nickname.length"))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That nickname is too long. The maximum length is " + Cfg.get().getInt("module.nickname.length") + " characters").build());

                        if (nick.matches("[^a-zA-Z0-9_ ]"))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That nickname contains illegal characters. Only A-Z, 0-9 and _ are allowed.").build());

                        PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);
                        data.setNickname(nick);
                        PlayerDataHolder.getInstance().setPlayerData(player, data);

                        NicknameLookup.getInstance().addNicknameToLookup(nick, player);

                        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> {
                            DatabaseQueries.savePlayerNickname(player, nick);
                        });
                    }),
                new CommandAPICommand("clear")
                    .withPermission("maquillage.nick.clear")
                    .withArguments(new PlayerArgument("player"))
                    .executes((sender, args) -> {
                        Player player = (Player) args.get("player");

                        if (player == null)
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>The player wasn't found.").build());

                        PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);

                        if (data.getNickname().isEmpty())
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of("<red>That player doesn't appear to have a nickname.").build());

                        NicknameLookup.getInstance().removeNicknameFromLookup(data.getNickname().get().getString());

                        data.clearNickname();
                        PlayerDataHolder.getInstance().setPlayerData(player, data);

                        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> {
                            DatabaseQueries.clearPlayerNickname(player);
                        });
                    })
            );
    }

}
