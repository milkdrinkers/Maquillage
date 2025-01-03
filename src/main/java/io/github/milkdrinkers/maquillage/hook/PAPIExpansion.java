package io.github.milkdrinkers.maquillage.hook;

import com.earth2me.essentials.Essentials;
import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A PlaceholderAPI expansion. Read the docs at <a href="https://wiki.placeholderapi.com/developers/creating-a-placeholderexpansion/">here</a> on how to register your custom placeholders.
 */
public class PAPIExpansion extends PlaceholderExpansion {
    private final Maquillage plugin;

    public PAPIExpansion(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "Maquillage";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "maquillage";
    }

    @Override
    public @NotNull String getAuthor() {
        return "rooooose-b";
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
                PlayerData data = PlayerDataHolder.getInstance().getPlayerData(p);

                String name = null;

                if (data.getNicknameString() != null && !data.getNicknameString().isEmpty()) {
                    name = data.getNicknameString();
                } else {
                    name = p.getName();
                }

                yield getNameColor(p, name);
            }
            case "namecolor_essentialsnick" -> {
                String newName = null;

                // Use essentials nickname if essentials is loaded
                if (Maquillage.getEssentialsHook().isHookLoaded()) {
                    Essentials essentials = Maquillage.getEssentialsHook().getHook();
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
            default -> null;
        };
    }

    private String getNameColor(Player p, String name) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);
        if (playerData == null || playerData.getNameColor().isEmpty())
            return name;

        String colorString = playerData.getNameColor().get().getColor();
        if (colorString.startsWith("<gradient"))
            return colorString + name + "</gradient>";
        else if (colorString.startsWith("<rainbow"))
            return colorString + name + "</rainbow>";

        return colorString + name;
    }

    private String getTag(Player p) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(p);
        if (playerData == null || playerData.getTag().isEmpty())
            return "";

        return playerData.getTag().get().getTag();
    }
}
