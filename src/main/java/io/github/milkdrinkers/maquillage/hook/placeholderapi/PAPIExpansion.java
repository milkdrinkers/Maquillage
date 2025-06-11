package io.github.milkdrinkers.maquillage.hook.placeholderapi;

import com.earth2me.essentials.Essentials;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.hook.Hook;
import io.github.milkdrinkers.maquillage.module.nickname.Nickname;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A PlaceholderAPI expansion. Read the docs at <a href="https://wiki.placeholderapi.com/developers/creating-a-placeholderexpansion/">here</a> on how to register your custom placeholders.
 */
public class PAPIExpansion extends PlaceholderExpansion {
    private final Maquillage plugin;

    public PAPIExpansion(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getIdentifier() {
        return plugin.getPluginMeta().getName().replace(' ', '_').toLowerCase();
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This needs to be true, or PlaceholderAPI will unregister the expansion during a plugin reload.
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String params) {
        return switch (params) {
            case "namecolor" -> getNameColor(p, p.getName());
            case "namecolor_nickname" -> {
                String name = getNickname(p).orElse(p.getName());

                yield getNameColor(p, name);
            }
            case "namecolor_essentialsnick" -> {
                String newName = null;

                // Use essentials nickname if essentials is loaded
                if (Hook.Essentials.isLoaded()) {
                    Essentials essentials = Hook.getEssentialsHook().getHook();
                    String nickname = essentials.getUser(p).getFormattedNickname();

                    if (nickname != null)
                        newName = nickname;
                }

                // Fallback to player name if essentials is not loaded or the nickname was empty
                if (newName == null)
                    newName = p.getName();

                yield getNameColor(p, newName);
            }
            case "tag" -> {
                String tag = getTag(p);
                if (tag.isEmpty())
                    yield "";
                else
                    yield tag + " ";
            }
            case "tag_nospace" -> getTag(p);
            case "nickname" ->
                getNickname(p).orElse(p.getName());  // Returns the nickname if it exists, otherwise returns the player's name
            case "nickname_space" -> getNickname(p).orElse(p.getName()) + " ";
            case "nickname_nofallback" ->
                getNickname(p).orElse("");  // Returns the nickname if it exists, otherwise returns the player's name
            case "nickname_nofallback_space" -> getNickname(p).orElse("") + " ";
            case "nickname_color" -> getNameColor(p, getNickname(p).orElse(p.getName()));
            case "nickname_color_space" -> getNameColor(p, getNickname(p).orElse(p.getName())) + " ";
            default -> null;
        };
    }

    private Optional<String> getNickname(Player p) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);
        if (playerData == null)
            return Optional.empty();

        return playerData.getNickname().map(Nickname::getNickname);
    }

    private String getNameColor(Player p, String playerName) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);
        if (playerData == null || playerData.getNameColor().isEmpty())
            return playerName;

        // Patch for gradient and rainbow
        final String colorString = playerData.getNameColor().get().getColor();
        if (colorString.startsWith("<gradient"))
            return colorString + playerName + "</gradient>";
        else if (colorString.startsWith("<rainbow"))
            return colorString + playerName + "</rainbow>";

        return colorString + playerName;
    }

    private String getTag(Player p) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);
        if (playerData == null || playerData.getTag().isEmpty())
            return "";

        return playerData.getTag().get().getTag();
    }
}
