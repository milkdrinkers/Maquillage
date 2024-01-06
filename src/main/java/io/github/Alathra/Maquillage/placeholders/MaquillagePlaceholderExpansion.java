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
        if (params.equals("namecolor"))
            return NameColorHandler.getPlayerColorString(player) + player.getName();

        if (params.equals("namecolor_essentialsnick"))
            return NameColorHandler.getPlayerColorString(player) + " %essentials_nickname_stripped%";

        if (params.equals("tag"))
            return TagHandler.getPlayerTagString(player);

        return "Error";
    }
}
