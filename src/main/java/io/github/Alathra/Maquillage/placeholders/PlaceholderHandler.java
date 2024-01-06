package io.github.Alathra.Maquillage.placeholders;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;

public class PlaceholderHandler implements Reloadable {

    private final Maquillage plugin;

    public PlaceholderHandler(Maquillage plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        new MaquillagePlaceholderExpansion(plugin).register();
    }

    @Override
    public void onDisable() {

    }
}
