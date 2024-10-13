package io.github.alathra.maquillage.command.nickname;

import com.github.milkdrinkers.colorparser.ColorParser;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.alathra.maquillage.module.nickname.NicknameLookup;
import io.github.alathra.maquillage.translation.Translation;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandRealname {

    public static CommandAPICommand registerCommandRealname() {
        return new CommandAPICommand("realname")
            .withPermission("maquillage.command.realname")
            .withArguments(List.of(new StringArgument("nickname")))
            .executes((sender, args) -> {
                String nickname = args.get("nickname").toString();
                String playerName = NicknameLookup.getInstance().findNameFromNickname(nickname);

                sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.realname.realname"))
                    .parseMinimessagePlaceholder("nickname", nickname)
                    .parseMinimessagePlaceholder("player", playerName).build());
            });
    }


}
