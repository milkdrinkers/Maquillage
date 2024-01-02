package io.github.Alathra.Maquillage.placeholders;

import io.github.Alathra.Maquillage.Maquillage;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TagPlaceholderExpansion extends PlaceholderExpansion {

    private final Maquillage plugin;

    public TagPlaceholderExpansion(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        return "tag";
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
        return "rooooo-b";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
}
