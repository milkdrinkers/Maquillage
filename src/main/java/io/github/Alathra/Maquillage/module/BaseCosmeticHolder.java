package io.github.Alathra.Maquillage.module;

import java.util.HashMap;
import java.util.List;

public interface BaseCosmeticHolder<T extends BaseCosmetic> {

    // SECTION Cache

    /**
     * Get the entire cache
     * @return then entire cache
     */
    HashMap<Integer, T> cacheGet();

    /**
     * Add this value to the cache
     * @param value BaseCosmetic
     */
    void cacheAdd(T value);

    /**
     * Remove this value from the cache
     * @param value BaseCosmetic
     */
    void cacheRemove(T value);

    /**
     * Clear the entire cache
     */
    void cacheClear();

    // SECTION Database

    /**
     * Attempts to save a BaseCosmetic to DB and, if successful, caches the BaseCosmetic
     * @param value BaseCosmetic
     * @param perm a permission node
     * @param name a display name
     * @param identifier a unique identifier
     * @return -1 if failed, otherwise the ID of the BaseCosmetic
     */
    int add(String value, String perm, String name, String identifier);

    /**
     * Attempts to update a BaseCosmetic to DB and, if successful, re-caches the BaseCosmetic
     * @param value BaseCosmetic
     * @param perm a permission node
     * @param name a display name
     * @param identifier a unique identifier
     * @param ID a unique id
     * @return whether the value was updated
     */
    boolean update(String value, String perm, String name, String identifier, int ID);

    /**
     * Attempts to remove a BaseCosmetic from DB and, if successful, un-caches the BaseCosmetic
     * @param value BaseCosmetic
     * @return false if failed
     */
    boolean remove(T value);

    /**
     * Loads all data from DB and caches it
     */
    void loadAll();

    // SECTION Identifiers

    T getByID(int ID);

    T getByIDString(String identifier);

    List<String> getAllIdentifiers();

    boolean doesIdentifierExist(String identifier);
}
