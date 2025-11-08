package io.github.milkdrinkers.maquillage.command.nickname;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.api.event.nickname.PlayerNicknameChangeEvent;
import io.github.milkdrinkers.maquillage.api.event.nickname.PlayerNicknamePreChangeEvent;
import io.github.milkdrinkers.maquillage.api.event.nickname.PlayerNicknamePreRemoveEvent;
import io.github.milkdrinkers.maquillage.api.event.nickname.PlayerNicknameRemoveEvent;
import io.github.milkdrinkers.maquillage.cooldown.CooldownType;
import io.github.milkdrinkers.maquillage.cooldown.Cooldowns;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.hook.Hook;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import io.github.milkdrinkers.threadutil.Scheduler;
import io.github.milkdrinkers.wordweaver.Translation;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommandNickname {
    public static CommandAPICommand registerCommandNickname(String command, String... aliases) {
        return new CommandAPICommand(command)
            .withAliases(aliases)
            .withPermission("maquillage.command.nick")
            .withSubcommands(
                registerSet("set"),
                registerClear("clear"),
                CommandRealname.registerCommandRealname("realname")
            );
    }

    public static CommandAPICommand registerSet(String command, String... aliases) {
        return new CommandAPICommand(command)
            .withAliases(aliases)
            .withPermission("maquillage.command.nick.set")
            .withArguments(
                new StringArgument("player")
                    .replaceSuggestions(
                        ArgumentSuggestions.stringCollectionAsync(
                            (info) -> CompletableFuture.supplyAsync(
                                () -> Queries.Nickname.fetchSimilarNames(info.currentArg())
                            )
                        )
                    ),
                new StringArgument("nick")
            )
            .executesPlayer((sender, args) -> { // When sender is a player
                final String name = args.getByClassOrDefault("player", String.class, "");
                final CompletableFuture<Optional<PlayerProfile>> future = CompletableFuture.supplyAsync(() -> Queries.Nickname.fetchPlayerProfile(name));

                future.thenAccept(playerProfileRes -> {
                    final OfflinePlayer player = playerProfileRes
                        .map(playerProfile -> Bukkit.getOfflinePlayer(Objects.requireNonNull(playerProfile.getId())))
                        .orElse(sender);

                    // Check if allowed to set nick of other player
                    if (
                        !sender.getUniqueId().equals(player.getUniqueId()) &&
                            Hook.getVaultHook().isPermissionsLoaded() &&
                            !Hook.getVaultHook().getPermissions().has(sender, "maquillage.command.nick.set.other")
                    ) {
                        sender.sendMessage(Bukkit.getServer().permissionMessage());
                        return;
                    }

                    setNickname(
                        sender,
                        player,
                        args
                    );
                });
            })
            .executes((sender, args) -> {
                final String name = args.getOptionalByClass("player", String.class)
                    .orElseThrow(
                        () -> CommandAPIPaper.failWithAdventureComponent(
                            Translation.as("commands.module.nickname.nickname.set.player-not-found")
                        )
                    );
                final CompletableFuture<Optional<PlayerProfile>> future = CompletableFuture.supplyAsync(() -> Queries.Nickname.fetchPlayerProfile(name));

                future.thenAccept(playerProfileRes -> {
                    if (playerProfileRes.isEmpty()) {
                        sender.sendMessage(Translation.as("commands.module.nickname.nickname.set.player-not-found"));
                        return;
                    }

                    final OfflinePlayer player = playerProfileRes
                        .map(playerProfile -> Bukkit.getOfflinePlayer(Objects.requireNonNull(playerProfile.getId())))
                        .get();

                    setNickname(
                        sender,
                        player,
                        args
                    );
                });
            });
    }

    /**
     * Set the nickname of a player
     */
    private static void setNickname(CommandSender sender, OfflinePlayer player, CommandArguments args) {
        if (sender instanceof Player senderPlayer && Cooldowns.has(senderPlayer, CooldownType.CommandNickname)) {
            sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());
            return;
        }

        if (!(args.get("nick") instanceof String nick)) {
            sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.no-nickname")).build());
            return;
        }

        if (nick.length() > Cfg.get().getInt("module.nickname.length")) {
            sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.too-long"))
                .with("characters", String.valueOf(Cfg.get().getInt("module.nickname.length"))).build());
            return;
        }

        if (nick.matches("[^a-zA-Z0-9_ ]")) {
            sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.illegal-characters")).build());
            return;
        }

        final Nickname nickname = new Nickname(nick, player.getName());

        final Player onlinePlayer = player.getPlayer();
        PlayerData data = null;

        if (onlinePlayer != null) {
            data = PlayerDataHolder.getInstance().getPlayerData(onlinePlayer);
            if (data == null) {
                sender.sendMessage(ColorParser.of(Translation.of("commands.module.error.player-not-loaded")).build());
                return;
            }
        }

        final PlayerNicknamePreChangeEvent event = new PlayerNicknamePreChangeEvent(player, nickname, data != null ? data.getNickname().orElse(null) : null);
        if (!event.callEvent())
            return;

        if (onlinePlayer != null) {
            data.setNickname(event.getNickname());
            PlayerDataHolder.getInstance().setPlayerData(onlinePlayer, data);
        }

        Scheduler.async(() -> Queries.Nickname.savePlayerNickname(player, nickname)).execute();

        final String prefix = !Cfg.get().getBoolean("module.nickname.prefix.enabled") ? "" : Cfg.get().getString("module.nickname.prefix.string");

        if (onlinePlayer != null) {
            if (Cfg.get().getBoolean("module.nickname.set-displayname"))
                onlinePlayer.displayName(Component.text(prefix + data.getNicknameString()));

            if (Cfg.get().getBoolean("module.nickname.set-listname"))
                onlinePlayer.playerListName(Component.text(prefix + data.getNicknameString()));
        }

        if (sender instanceof Player senderPlayer)
            Cooldowns.set(senderPlayer, CooldownType.CommandNickname, 2);

        sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.success"))
            .with("player", Objects.requireNonNull(player.getName()))
            .with("nickname", nick).build());

        new PlayerNicknameChangeEvent(player, event.getNickname(), event.getPreviousNickname()).callEvent();
    }

    public static CommandAPICommand registerClear(String command, String... aliases) {
        return new CommandAPICommand(command)
            .withAliases(aliases)
            .withPermission("maquillage.command.nick.clear")
            .withOptionalArguments(
                new StringArgument("player")
                    .replaceSuggestions(
                        ArgumentSuggestions.stringCollectionAsync(
                            (info) -> CompletableFuture.supplyAsync(
                                () -> Queries.Nickname.fetchSimilarNames(info.currentArg())
                            )
                        )
                    )
                    .withPermission("maquillage.command.nick.clear.other")
            )
            .executesPlayer((sender, args) -> { // When sender is a player
                final String name = args.getByClassOrDefault("player", String.class, "");
                final CompletableFuture<Optional<PlayerProfile>> future = CompletableFuture.supplyAsync(() -> Queries.Nickname.fetchPlayerProfile(name));

                future.thenAccept(playerProfileRes -> {
                    final OfflinePlayer player = playerProfileRes
                        .map(playerProfile -> Bukkit.getOfflinePlayer(Objects.requireNonNull(playerProfile.getId())))
                        .orElse(sender);

                    clearNickname(
                        sender,
                        player
                    );
                });
            })
            .executes((sender, args) -> { // When sender is anything else
                final String name = args.getOptionalByClass("player", String.class)
                    .orElseThrow(
                        () -> CommandAPIPaper.failWithAdventureComponent(
                            Translation.as("commands.module.nickname.nickname.clear.player-not-found")
                        )
                    );
                final CompletableFuture<Optional<PlayerProfile>> future = CompletableFuture.supplyAsync(() -> Queries.Nickname.fetchPlayerProfile(name));

                future.thenAccept(playerProfileRes -> {
                    if (playerProfileRes.isEmpty()) {
                        sender.sendMessage(Translation.as("commands.module.nickname.nickname.clear.player-not-found"));
                        return;
                    }

                    final OfflinePlayer player = playerProfileRes
                        .map(playerProfile -> Bukkit.getOfflinePlayer(Objects.requireNonNull(playerProfile.getId())))
                        .get();

                    clearNickname(
                        sender,
                        player
                    );
                });
            });
    }

    /**
     * Actually clears the nickname of a player
     */
    private static void clearNickname(CommandSender sender, OfflinePlayer player) {
        if (sender instanceof Player senderPlayer && Cooldowns.has(senderPlayer, CooldownType.CommandNickname)) {
            sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());
            return;
        }

        final Player onlinePlayer = player.getPlayer();
        PlayerData data = null;

        if (onlinePlayer != null) {
            data = PlayerDataHolder.getInstance().getPlayerData(onlinePlayer);
            if (data == null) {
                sender.sendMessage(ColorParser.of(Translation.of("commands.module.error.player-not-loaded")).build());
                return;
            }

            if (data.getNickname().isEmpty()) {
                sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.no-nickname")).build());
                return;
            }
        }

        if (sender instanceof Player senderPlayer)
            Cooldowns.set(senderPlayer, CooldownType.CommandNickname, 2);

        final PlayerNicknamePreRemoveEvent event = new PlayerNicknamePreRemoveEvent(player, data != null ? data.getNickname().orElse(null) : null);
        if (!event.callEvent())
            return;

        if (onlinePlayer != null) {
            data.clearNickname();

            PlayerDataHolder.getInstance().setPlayerData(onlinePlayer, data);
        }

        Scheduler.async(() -> Queries.Nickname.clearPlayerNickname(player)).execute();

        if (onlinePlayer != null) {
            if (Cfg.get().getBoolean("module.nickname.set-displayname"))
                onlinePlayer.displayName(Component.text(Objects.requireNonNull(player.getName())));

            if (Cfg.get().getBoolean("module.nickname.set-listname"))
                onlinePlayer.playerListName(Component.text(Objects.requireNonNull(player.getName())));
        }

        sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.cleared"))
            .with("player", Objects.requireNonNull(player.getName())).build());

        new PlayerNicknameRemoveEvent(player, event.getPreviousNickname()).callEvent();
    }
}
