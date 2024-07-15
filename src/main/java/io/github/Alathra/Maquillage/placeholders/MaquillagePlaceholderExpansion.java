package io.github.Alathra.Maquillage.placeholders;

import com.earth2me.essentials.Essentials;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.player.PlayerData;
import io.github.Alathra.Maquillage.player.PlayerDataHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.ess3.api.IUser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaquillagePlaceholderExpansion extends PlaceholderExpansion {

    private final Maquillage plugin;

    public MaquillagePlaceholderExpansion(Maquillage plugin) {
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
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String params) {
        return switch (params) {
            case "namecolor" -> getNameColor(p, p.getName());
            case "namecolor_essentialsnick" -> {
                String newName = null;

                // Use essentials nickname if essentials is loaded
                if (Maquillage.getEssentialsHook().isHookLoaded()) {
                    Essentials essentials = Maquillage.getEssentialsHook().getHook();
                    IUser user = essentials.getUser(p);
                    String nickname = user.getFormattedNickname();

                    if (nickname != null)
                        newName = nickname;
                }

                // Fallback to player name if essentials is not loaded or the nickname was empty
                if (newName == null)
                    newName = p.getName();

                yield getNameColor(p, newName);
            }
            case "tag" -> getTag(p) + " ";
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
            return  "";

        return playerData.getTag().get().getTag();
    }
}
