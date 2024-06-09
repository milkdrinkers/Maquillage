package io.github.Alathra.Maquillage.utility;

import com.earth2me.essentials.Essentials;
import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.Tag;
import io.github.Alathra.Maquillage.tag.TagHandler;
import net.ess3.api.IUser;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class UpdateDisplayName {

    public static void updateDisplayName(Player player, Tag tag, NameColor color) {
        Component newName = null;

        // Use essentials nickname if essentials is loaded
        if (Maquillage.getEssentialsHook().isHookLoaded()) {
            Essentials essentials = Maquillage.getEssentialsHook().getHook();
            IUser user = essentials.getUser(player);
            String nickname = user.getFormattedNickname();

            if (nickname != null)
                newName = ColorParser.of(tag.getTag() + " " + color.getColor() + nickname).build();
        }

        // Fallback to player name if essentials is not loaded or the nickname was empty
        if (newName == null)
            newName = ColorParser.of(tag.getTag() + " " + color.getColor() + player.getName()).build();

        player.displayName(newName);
    }

    public static void updateDisplayNameOnNickChange(Player player, String newNick, Tag tag, NameColor color) {
        Component newName = ColorParser.of(tag.getTag() + " " + color.getColor() + newNick).build();

        player.displayName(newName);
    }

    public static void updateDisplayNameOnNickReset(Player player, Tag tag, NameColor color) {
        Component newName = ColorParser.of(tag.getTag() + " " + color.getColor() + player.getName()).build();

        player.displayName(newName);
    }

    public static void clearPlayerNameColor(Player player) {
        Component newName = null;

        // Use essentials nickname if essentials is loaded
        if (Maquillage.getEssentialsHook().isHookLoaded()) {
            Essentials essentials = Maquillage.getEssentialsHook().getHook();
            IUser user = essentials.getUser(player);
            String nickname = user.getFormattedNickname();

            if (nickname != null)
                newName = ColorParser.of(TagHandler.getPlayerTagString(player) + " <white>" + nickname).build();
        }

        // Fallback to player name if essentials is not loaded or the nickname was empty
        if (newName == null)
            newName = ColorParser.of(TagHandler.getPlayerTagString(player) + " <white>" + player.getName()).build();

        player.displayName(newName);
    }

    public static void clearPlayerTag(Player player) {
        Component newName = null;

        // Use essentials nickname if essentials is loaded
        if (Maquillage.getEssentialsHook().isHookLoaded()) {
            Essentials essentials = Maquillage.getEssentialsHook().getHook();
            IUser user = essentials.getUser(player);
            String nickname = user.getFormattedNickname();

            if (nickname != null)
                newName = ColorParser.of(NameColorHandler.getPlayerColorString(player) + nickname).build();
        }

        // Fallback to player name if essentials is not loaded or the nickname was empty
        if (newName == null)
            newName = ColorParser.of(NameColorHandler.getPlayerColorString(player) + player.getName()).build();

        player.displayName(newName);
    }
}
