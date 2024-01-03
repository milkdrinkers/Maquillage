package io.github.Alathra.Maquillage.placeholders;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NameColorPlaceholderExpansion extends PlaceholderExpansion {

    private final Maquillage plugin;

    public NameColorPlaceholderExpansion(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "Maquillage";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "maquillage_namecolor";
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
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return NameColorHandler.getPlayerColorString(player.getPlayer()) + player.getName();
    }
}
