package io.github.milkdrinkers.maquillage.command.nickname;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import io.github.milkdrinkers.maquillage.module.nickname.NicknameLookup;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.translation.Translation;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import io.github.milkdrinkers.threadutil.Scheduler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

import static io.github.milkdrinkers.maquillage.command.argument.CustomPlayerArgument.customPlayerArgument;

public class CommandNickname {

    public static CommandAPICommand registerCommandNickname() {
        return new CommandAPICommand("nickname")
            .withAliases("nick")
            .withPermission("maquillage.command.nick")
            .withSubcommands(
                registerSet(),
                registerClear()
            );
    }

    private static CommandAPICommand registerSet() {
        return new CommandAPICommand("set")
            .withPermission("maquillage.command.nick.set")
            .withArguments(
                customPlayerArgument("player"),
                new StringArgument("nick")
            )
            .executesPlayer((sender, args) -> { // When sender is a player
                final Player player = args.getOptionalByClass("player", Player.class).orElse(sender); // If no player is specified, use the sender

                // Check if allowed to set nick of other player
                if (
                    !sender.equals(player) &&
                        Maquillage.getVaultHook().isPermissionsLoaded() &&
                        !Maquillage.getVaultHook().getPermissions().has(sender, "maquillage.command.nick.set.other")
                ) {
                    throw CommandAPIBukkit.failWithAdventureComponent(Bukkit.getServer().permissionMessage());
                }

                setNickname(
                    sender,
                    player,
                    args
                );
            })
            .executes((sender, args) -> {
                final Optional<Player> player = args.getOptionalByClass("player", Player.class);

                setNickname(
                    sender,
                    player.orElseThrow(() -> CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.player-not-found")).build())),
                    args
                );
            });
    }

    /**
     * Set the nickname of a player
     */
    private static void setNickname(CommandSender sender, Player player, CommandArguments args) throws WrapperCommandSyntaxException {
        if (sender instanceof Player senderPlayer && NicknameCooldown.hasCooldown(senderPlayer))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());

        if (!(args.get("nick") instanceof String nick))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.no-nickname")).build());

        if (nick.length() > Cfg.get().getInt("module.nickname.length"))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.too-long"))
                .with("characters", String.valueOf(Cfg.get().getInt("module.nickname.length"))).build());

        if (nick.matches("[^a-zA-Z0-9_ ]"))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.illegal-characters")).build());

        final Nickname nickname = new Nickname(nick, player.getName());

        final PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);
        if (data == null)
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.error.player-not-loaded")).build());

        data.setNickname(nickname);

        PlayerDataHolder.getInstance().setPlayerData(player, data);

        NicknameLookup.getInstance().addNicknameToLookup(nick, player);

        String prefix = "";
        if (Cfg.get().getBoolean("module.nickname.prefix.enabled"))
            prefix = Cfg.get().getString("module.nickname.prefix.string");

        if (Cfg.get().getBoolean("module.nickname.set-displayname"))
            player.displayName(Component.text(prefix + data.getNicknameString()));

        if (Cfg.get().getBoolean("module.nickname.set-listname"))
            player.playerListName(Component.text(prefix + data.getNicknameString()));

        if (sender instanceof Player senderPlayer)
            NicknameCooldown.setCooldown(senderPlayer);

        sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.success"))
            .with("player", player.getName())
            .with("nickname", nick).build());

        Scheduler.async(() -> Queries.Nickname.savePlayerNickname(player, nickname)).execute();
    }

    private static CommandAPICommand registerClear() {
        return new CommandAPICommand("clear")
            .withPermission("maquillage.command.nick.clear")
            .withOptionalArguments(
                customPlayerArgument("player")
                    .withPermission("maquillage.command.nick.clear.other")
            )
            .executesPlayer((sender, args) -> { // When sender is a player
                clearNickname(
                    sender,
                    args.getOptionalByClass("player", Player.class).orElse(sender) // If no player is specified, use the sender
                );
            })
            .executes((sender, args) -> { // When sender is anything else
                final Optional<Player> player = args.getOptionalByClass("player", Player.class);

                clearNickname(
                    sender,
                    player.orElseThrow(() -> CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.player-not-found")).build()))
                );
            })
            ;
    }

    /**
     * Actually clears the nickname of a player
     */
    private static void clearNickname(CommandSender sender, Player player) throws WrapperCommandSyntaxException {
        if (sender instanceof Player senderPlayer && NicknameCooldown.hasCooldown(senderPlayer))
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.set.cooldown")).build());

        final PlayerData data = PlayerDataHolder.getInstance().getPlayerData(player);
        if (data == null)
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.error.player-not-loaded")).build());

        if (data.getNickname().isEmpty())
            throw CommandAPIBukkit.failWithAdventureComponent(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.no-nickname")).build());

        if (sender instanceof Player senderPlayer)
            NicknameCooldown.setCooldown(senderPlayer);

        NicknameLookup.getInstance().removeNicknameFromLookup(data.getNickname().get().getNickname());

        data.clearNickname();
        PlayerDataHolder.getInstance().setPlayerData(player, data);

        if (Cfg.get().getBoolean("module.nickname.set-displayname"))
            player.displayName(Component.text(player.getName()));

        if (Cfg.get().getBoolean("module.nickname.set-listname"))
            player.playerListName(Component.text(player.getName()));

        sender.sendMessage(ColorParser.of(Translation.of("commands.module.nickname.nickname.clear.cleared"))
            .with("player", player.getName()).build());

        Scheduler.async(() -> Queries.Nickname.clearPlayerNickname(player)).execute();
    }
}
