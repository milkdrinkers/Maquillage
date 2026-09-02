package io.github.milkdrinkers.maquillage;

import io.github.milkdrinkers.maquillage.api.MaquillageAPI;

/**
 * The API provider for Maquillage. This class is responsible for implementing the Maquillage API.
 *
 * @see MaquillageAPI
 * @since 1.1.0
 */
class MaquillageAPIProvider extends MaquillageAPI implements Reloadable {
    @SuppressWarnings("unused")
    private final Maquillage plugin;

    MaquillageAPIProvider(Maquillage plugin) {
        super();
        this.plugin = plugin;
        setInstance(this);
    }
}
