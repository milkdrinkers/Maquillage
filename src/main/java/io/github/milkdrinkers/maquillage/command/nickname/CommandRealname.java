package io.github.milkdrinkers.maquillage.command.nickname;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import io.github.milkdrinkers.maquillage.translation.Translation;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommandRealname {
    public static CommandAPICommand registerCommandRealname() {
        return new CommandAPICommand("realname")
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
                if (sender instanceof Player player && NicknameCooldown.hasCooldown(player))
                    throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());

                if (sender instanceof Player senderPlayer)
                    NicknameCooldown.setCooldown(senderPlayer);

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
