package io.github.milkdrinkers.maquillage.module.cosmetic.namecolor;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.cooldown.CooldownType;
import io.github.milkdrinkers.maquillage.cooldown.Cooldowns;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.database.schema.tables.records.ColorsRecord;
import io.github.milkdrinkers.maquillage.messaging.MessagingUtils;
import io.github.milkdrinkers.maquillage.module.cosmetic.BaseCosmeticHolder;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.utility.PermissionUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.Result;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NameColorHolder implements BaseCosmeticHolder<NameColor> {
    private static NameColorHolder INSTANCE;
    private final Map<Integer, NameColor> cachedColors = new ConcurrentHashMap<>();
    private final Map<String, Integer> colorKeys = new ConcurrentHashMap<>();

    private NameColorHolder() {
    }

    public static NameColorHolder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NameColorHolder();

        return INSTANCE;
    }

    // SECTION Cache

    @Override
    public Map<Integer, NameColor> cacheGet() {
        return Collections.unmodifiableMap(cachedColors);
    }

    @Override
    public void cacheAdd(NameColor color) {
        cachedColors.put(color.getDatabaseId(), color);
        colorKeys.put(color.getKey(), color.getDatabaseId());

        PermissionUtility.registerPermission(color.getPerm());
    }

    @Override
    public void cacheRemove(NameColor value) {
        cachedColors.remove(value.getDatabaseId());
        colorKeys.remove(value.getKey());

        PermissionUtility.removePermission(value.getPerm());
    }

    @Override
    public void cacheRemove(int id) {
        cacheRemove(getByDatabaseId(id));
    }

    @Override
    public void cacheClear() {
        cachedColors.clear();
        colorKeys.clear();
    }

    // SECTION Database

    @Override
    public int add(String value, String perm, String label, int weight) {
        int databaseId = Queries.NameColor.saveColor(value, perm, label, weight);
        if (databaseId != -1) {
            cacheAdd(
                new NameColorBuilder()
                    .withColor(value)
                    .withPerm(perm)
                    .withLabel(label)
                    .withDatabaseId(databaseId)
                    .createNameColor()
            );
            MessagingUtils.sendNameColorFetch(databaseId);
        }
        return databaseId;
    }

    @Override
    public boolean remove(NameColor value) {
        boolean success = Queries.NameColor.removeColor(value.getDatabaseId());
        if (!success)
            return false;
        PlayerDataHolder.getInstance().clearNameColorWithId(value.getDatabaseId());
        cacheRemove(value);
        MessagingUtils.sendNameColorDelete(value.getDatabaseId());
        return true;
    }

    @Override
    public boolean update(String value, String perm, String label, int databaseId, int weight) {
        boolean success = Queries.NameColor.updateColor(value, perm, label, databaseId, weight);
        if (!success)
            return false;

        cacheAdd(
            new NameColorBuilder()
                .withColor(value)
                .withPerm(perm)
                .withLabel(label)
                .withDatabaseId(databaseId)
                .createNameColor()
        );
        MessagingUtils.sendNameColorFetch(databaseId);
        return true;
    }

    @Override
    public void load(NameColor nameColor) {
        cacheAdd(nameColor);
    }

    @Override
    public void loadAll() {
        Result<ColorsRecord> result = Queries.NameColor.loadAllColors();

        if (result == null)
            return;

        result.map(NameColorBuilder::deserialize)
            .forEach(this::cacheAdd);
    }

    // SECTION Identifiers

    @Override
    public NameColor getByDatabaseId(int databaseId) {
        return cachedColors.get(databaseId);
    }

    @Override
    public NameColor getByKey(String key) {
        return getByDatabaseId(colorKeys.get(key));
    }

    @Override
    public List<String> getAllKeys() {
        return colorKeys.keySet().stream().toList();
    }

    @Override
    public boolean doesKeyExist(String key) {
        return colorKeys.containsKey(key);
    }

    // SECTION Player

    /**
     * Removes a player's color from cache and DB.
     *
     * @param uuid uuid
     */
    public static void clearPlayerColor(UUID uuid) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(uuid);
        if (playerData != null)
            playerData.clearNameColor();

        Queries.NameColor.Players.removePlayerColor(uuid);
    }

    public static void clearPlayerColor(Player p) {
        clearPlayerColor(p.getUniqueId());
    }

    public static boolean setPlayerColor(Player p, NameColor nameColor) {
        UUID uuid = p.getUniqueId();
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(uuid);
        if (playerData == null)
            return false;

        // Has cooldown
        if (Cooldowns.has(p, CooldownType.Gui))
            return false;

        // Trying to set same value
        if (playerData.getNameColor().isPresent() && playerData.getNameColor().get().equals(nameColor))
            return false;

        Cooldowns.set(p, CooldownType.Gui, 2);

        final int databaseId = nameColor.getDatabaseId();
        playerData.setNameColor(nameColor);
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> Queries.NameColor.Players.savePlayerColor(uuid, databaseId));

        return true;
    }

    public Map<String, Integer> getColorKeys() {
        return Collections.unmodifiableMap(colorKeys);
    }
}
