package io.github.alathra.maquillage.command.nickname;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.alathra.maquillage.module.nickname.NicknameLookup;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandRealname {

    public static CommandAPICommand registerCommandRealname() {
        return new CommandAPICommand("realname")
            .withPermission("maquillage.realname")
            .withArguments(List.of(
                new StringArgument("nickname")
            .executes((sender, args) -> {
                String nickname = args.get("nickname").toString();
                String playerName = NicknameLookup.getInstance().findNameFromNickname(nickname);

                sender.sendMessage(ColorParser.of("<green>The username for <red>" + nickname + "<green> is <red>" + playerName).build());
            })));
    }


}
