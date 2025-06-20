package io.github.milkdrinkers.maquillage.command.nickname;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.cooldown.Cooldown;
import io.github.milkdrinkers.maquillage.cooldown.CooldownType;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommandRealname {
    public static CommandAPICommand registerCommandRealname(String command, String... aliases) {
        return new CommandAPICommand(command)
            .withAliases(aliases)
            .withPermission("maquillage.command.realname")
            .withArguments(
                new StringArgument("name")
                    .replaceSuggestions(
                        ArgumentSuggestions.stringCollectionAsync(
                            (info) -> CompletableFuture.supplyAsync(
                                () -> Queries.Nickname.fetchSimilarNames(info.currentArg())
                            )
                        )
                    )
            )
            .executes((sender, args) -> {
                if (sender instanceof Player player && Cooldown.getInstance().hasCooldown(player, CooldownType.CommandNickname))
                    throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());

                if (sender instanceof Player senderPlayer)
                    Cooldown.getInstance().setCooldown(senderPlayer, CooldownType.CommandNickname, 2);

                final String name = args.getByClassOrDefault("name", String.class, "");

                CompletableFuture<Optional<Nickname>> future = CompletableFuture.supplyAsync(() -> {
                    return Queries.Nickname.fetchMostSimilarNickname(name);
                });

                future.thenAccept(nickResult -> {
                    if (nickResult.isPresent()) {
                        final Nickname nickname = nickResult.get();
                        sender.sendMessage(
                            ColorParser.of(Translation.of("commands.module.nickname.realname.info"))
                                .with("input", name)
                                .with("username", nickname.getUsername())
                                .with("nickname", nickname.getNickname())
                                .build()
                        );
                    } else {
                        sender.sendMessage(
                            ColorParser.of(Translation.of("commands.module.nickname.realname.fail-matches"))
                                .with("input", name)
                                .build()
                        );
                    }
                }).exceptionally(t -> {
                    sender.sendMessage(
                        ColorParser.of(Translation.of("commands.module.nickname.realname.fail-search"))
                            .with("input", name)
                            .build()
                    );
                    return null;
                });
            });
    }
}
