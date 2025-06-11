package io.github.milkdrinkers.maquillage.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * The API class for Maquillage.
 * This class provides access to the Maquillage API.
 *
 * @see #getInstance()
 * @since 1.1.0
 */
public abstract class MaquillageAPI {
    private static MaquillageAPI instance = null;

    /**
     * Gets the instance of the MaquillageAPI.
     *
     * @return the instance of MaquillageAPI
     * @since 1.1.0
     */
    public static MaquillageAPI getInstance() {
        return instance;
    }

    /**
     * Sets the instance of the MaquillageAPI.
     * This method is intended for internal use by the api provider only.
     *
     * @param api the instance of MaquillageAPI to set
     * @since 1.1.0
     */
    @ApiStatus.Internal
    protected static void setInstance(MaquillageAPI api) {
        instance = api;
    }

    /**
     * Checks if the MaquillageAPI is available
     * @return true if API is loaded and available
     * @since 1.1.0
     */
    public static boolean isLoaded() {
        return instance != null;
    }
}
