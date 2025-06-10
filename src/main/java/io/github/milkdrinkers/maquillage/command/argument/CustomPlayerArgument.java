package io.github.milkdrinkers.maquillage.command.argument;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.SafeSuggestions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CustomPlayerArgument {
    /**
     * Creates a custom player argument that suggests online players.
     *
     * @param node The name of the node.
     * @return A PlayerArgument that suggests online players.
     * @implNote Unlike the default {@link PlayerArgument} this does not include entity selectors like {@code @a}, {@code @e}, etc.
     */
    public static Argument<Player> customPlayerArgument(String node) {
        return new PlayerArgument(node)
            .replaceSafeSuggestions(
                SafeSuggestions.suggest(info ->
                    Bukkit.getOnlinePlayers().toArray(new Player[0])
                )
            );
    }
}
