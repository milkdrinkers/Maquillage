package io.github.milkdrinkers.maquillage.command.nickname;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.module.nickname.NicknameLookup;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import io.github.milkdrinkers.maquillage.utility.Cfg;
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
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.player-not-found")).build());

                        if (!Maquillage.getVaultHook().getPermissions().has(player, "maquillage.nick.admin")) {
                            if (NicknameCooldown.hasCooldown(player))
                                throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());
                        }

                        if (!(args.get("nick") instanceof String nick))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.no-nickname")).build());

                        if(nick.length() > Cfg.get().getInt("module.nickname.length"))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.too-long"))
                                .parseMinimessagePlaceholder("characters", String.valueOf(Cfg.get().getInt("module.nickname.length"))).build());

                        if (nick.matches("[^a-zA-Z0-9_ ]"))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.illegal-characters")).build());

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
                            Queries.Nickname.savePlayerNickname(player, nick);
                        });
                    }),
                new CommandAPICommand("clear")
                    .withPermission("maquillage.command.nick.clear")
                    .withArguments(new PlayerArgument("player"))
                    .executes((sender, args) -> {
                        if (!(args.get("player") instanceof Player player))
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.player-not-found")).build());

                        PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);

                        if (data.getNickname() == null || data.getNickname().isEmpty())
                            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.no-nickname")).build());

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
                            Queries.Nickname.clearPlayerNickname(player);
                        });
                    })
            );
    }

}
