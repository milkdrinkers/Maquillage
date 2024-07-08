package io.github.Alathra.Maquillage.placeholders;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("namecolor")) {
            if (NameColorHandler.doesPlayerHaveColor(player))
                return NameColorHandler.getPlayerColorString(player) + player.getName();
            return "<white>" + player.getName();
        }

        if (params.equals("namecolor_essentialsnick")) {
            if (NameColorHandler.doesPlayerHaveColor(player))
                return NameColorHandler.getPlayerColorString(player) + "%essentials_nickname_stripped%";
            return "<white>%essentials_nickname_stripped%";
        }

        if (params.equals("tag")) {
            if (TagHandler.doesPlayerHaveTag(player))
                return TagHandler.getPlayerTagString(player) + " ";
            return "";
        }

        if (params.equals("tag_nospace")) {
            if (TagHandler.doesPlayerHaveTag(player))
                return TagHandler.getPlayerTagString(player);
            return "";
        }

        return "Error";
    }
}
