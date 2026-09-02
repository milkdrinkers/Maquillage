package io.github.milkdrinkers.maquillage.command.nickname;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import io.github.milkdrinkers.maquillage.cooldown.CooldownType;
import io.github.milkdrinkers.maquillage.cooldown.Cooldowns;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
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
                if (sender instanceof Player player && Cooldowns.has(player, CooldownType.CommandNickname))
                    throw CommandAPIPaper.failWithAdventureComponent(Component.translatable("maquillage.commands.module.nickname.nickname.set.cooldown"));

                if (sender instanceof Player senderPlayer)
                    Cooldowns.set(senderPlayer, CooldownType.CommandNickname, 2);

                final String name = args.getByClassOrDefault("name", String.class, "");

                final CompletableFuture<Optional<Nickname>> future = CompletableFuture.supplyAsync(() -> Queries.Nickname.fetchMostSimilarNickname(name));

                future.thenAccept(nickResult -> {
                    if (nickResult.isPresent()) {
                        final Nickname nickname = nickResult.get();
                        sender.sendMessage(
                            Component.translatable(
                                "maquillage.commands.module.nickname.realname.info",
                                Argument.string("input", name),
                                Argument.string("username", nickname.getUsername()),
                                Argument.string("nickname", nickname.getNickname())
                            )
                        );
                    } else {
                        sender.sendMessage(
                            Component.translatable(
                                "maquillage.commands.module.nickname.realname.fail-matches",
                                Argument.string("input", name)
                            )
                        );
                    }
                }).exceptionally(t -> {
                    sender.sendMessage(
                        Component.translatable(
                            "maquillage.commands.module.nickname.realname.fail-search",
                            Argument.string("input", name)
                        )
                    );
                    return null;
                });
            });
    }
}
