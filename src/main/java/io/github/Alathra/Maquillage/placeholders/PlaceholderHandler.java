package io.github.Alathra.Maquillage.placeholders;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.Reloadable;

public class PlaceholderHandler implements Reloadable {

    private final Maquillage plugin;

    private final MaquillagePlaceholderExpansion expansion;

    public PlaceholderHandler(Maquillage plugin) {
        this.plugin = plugin;
        this.expansion = new MaquillagePlaceholderExpansion(plugin);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        expansion.register();
    }

    @Override
    public void onDisable() {
        expansion.unregister();
    }
}
