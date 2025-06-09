package io.github.milkdrinkers.maquillage.command.nickname;

import io.github.milkdrinkers.colorparser.paper.ColorParser;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.milkdrinkers.maquillage.module.nickname.NicknameLookup;
import io.github.milkdrinkers.maquillage.translation.Translation;

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
                    .with("nickname", nickname)
                    .with("player", playerName).build());
            });
    }


}
