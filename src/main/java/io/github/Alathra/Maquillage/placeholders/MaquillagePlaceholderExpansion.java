package io.github.Alathra.Maquillage.placeholders;

import com.earth2me.essentials.Essentials;
import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.ess3.api.IUser;
import net.kyori.adventure.text.Component;
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
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("namecolor")) {
            if (NameColorHandler.doesPlayerHaveColor(player)) {
                String colorString = NameColorHandler.getPlayerColorString(player);
                if (colorString.startsWith("<gradient"))
                    return colorString + player.getName() + "</gradient>";
                else if (colorString.startsWith("<rainbow"))
                    return colorString + player.getName() + "</rainbow>";

                return colorString + player.getName();
            }
            return player.getName();
        }

        if (params.equals("namecolor_essentialsnick")) {
            String newName = null;

            // Use essentials nickname if essentials is loaded
            if (Maquillage.getEssentialsHook().isHookLoaded()) {
                Essentials essentials = Maquillage.getEssentialsHook().getHook();
                IUser user = essentials.getUser(player);
                String nickname = user.getFormattedNickname();

                if (nickname != null)
                    newName = nickname;
            }

            // Fallback to player name if essentials is not loaded or the nickname was empty
            if (newName == null)
                newName = player.getName();

            if (NameColorHandler.doesPlayerHaveColor(player)) {
                String colorString = NameColorHandler.getPlayerColorString(player);
                if (colorString.startsWith("<gradient"))
                    return colorString + newName + "</gradient>";
                else if (colorString.startsWith("<rainbow"))
                    return colorString + newName + "</rainbow>";

                return colorString + newName;
            }
            return newName;
        }

        if (params.equals("tag")) {
            if (TagHandler.doesPlayerHaveTag(player))
                return TagHandler.getPlayerTagString(player) + "<reset> ";
            return "";
        }

        if (params.equals("tag_nospace")) {
            if (TagHandler.doesPlayerHaveTag(player))
                return TagHandler.getPlayerTagString(player) + "<reset>";
            return "";
        }

        return "Error";
    }
}
