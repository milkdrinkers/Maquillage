package io.github.milkdrinkers.maquillage.command.argument;

import com.destroystokyo.paper.profile.PlayerProfile;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.PlayerProfileArgument;
import dev.jorel.commandapi.arguments.SafeSuggestions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CustomPlayerArgument {
    /**
     * Creates a custom player argument that suggests online players.
     *
     * @param node The name of the node.
     * @return A PlayerArgument that suggests online players.
     * @implNote Unlike the default {@link PlayerProfileArgument} this does not include entity selectors like {@code @a}, {@code @e}, etc.
     */
    public static Argument<?> customPlayerArgument(String node) {
        return new PlayerProfileArgument(node)
            .replaceSafeSuggestions(
                SafeSuggestions.suggest(info ->
                    Bukkit.getOnlinePlayers().stream().map(Player::getPlayerProfile).toArray(PlayerProfile[]::new)
                )
            );
    }
}
