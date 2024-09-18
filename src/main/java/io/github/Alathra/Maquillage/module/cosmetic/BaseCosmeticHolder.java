package io.github.alathra.maquillage.module.cosmetic;

import java.util.HashMap;
import java.util.List;

public interface BaseCosmeticHolder<T extends BaseCosmetic> {

    // SECTION Cache

    /**
     * Get the entire cache
     *
     * @return then entire cache
     */
    HashMap<Integer, T> cacheGet();

    /**
     * Add this value to the cache
     *
     * @param value BaseCosmetic
     */
    void cacheAdd(T value);

    /**
     * Remove this value from the cache
     *
     * @param value BaseCosmetic
     */
    void cacheRemove(T value);

    /**
     * Remove this by id from the cache
     *
     * @param id BaseCosmetic id
     */
    void cacheRemove(int id);

    /**
     * Clear the entire cache
     */
    void cacheClear();

    // SECTION Database

    /**
     * Attempts to save a BaseCosmetic to DB and, if successful, caches the BaseCosmetic
     *
     * @param value      BaseCosmetic
     * @param perm       a permission node
     * @param label       a display label
     * @param key a unique key
     * @return -1 if failed, otherwise the ID of the BaseCosmetic
     */
    int add(String value, String perm, String label, String key);

    /**
     * Attempts to update a BaseCosmetic to DB and, if successful, re-caches the BaseCosmetic
     *
     * @param value      BaseCosmetic
     * @param perm       a permission node
     * @param label       a display label
     * @param key a unique key
     * @param databaseId         a unique id
     * @return whether the value was updated
     */
    boolean update(String value, String perm, String label, String key, int databaseId);

    /**
     * Attempts to remove a BaseCosmetic from DB and, if successful, un-caches the BaseCosmetic
     *
     * @param value BaseCosmetic
     * @return false if failed
     */
    boolean remove(T value);

    /**
     * Load a BaseCosmetic from DB into cache
     *
     * @param value BaseCosmetic
     */
    void load(T value);

    /**
     * Loads all data from DB and caches it
     */
    void loadAll();

    // SECTION Identifiers

    T getByDatabaseId(int databaseId);

    T getByKey(String key);

    List<String> getAllKeys();

    boolean doesKeyExist(String key);
}
