package io.github.milkdrinkers.maquillage.module.cosmetic.namecolor;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.database.sync.SyncHandler;
import io.github.milkdrinkers.maquillage.gui.GuiCooldown;
import io.github.milkdrinkers.maquillage.module.cosmetic.BaseCosmeticHolder;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.utility.PermissionUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.Record4;
import org.jooq.Result;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.github.milkdrinkers.maquillage.database.schema.tables.Colors.COLORS;

public class NameColorHolder implements BaseCosmeticHolder<NameColor> {
    private static NameColorHolder INSTANCE;
    private final HashMap<Integer, NameColor> cachedColors = new HashMap<>();
    private final HashMap<String, Integer> colorKeys = new HashMap<>();

    private NameColorHolder() {
    }

    public static NameColorHolder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NameColorHolder();

        return INSTANCE;
    }

    // SECTION Cache

    @Override
    public HashMap<Integer, NameColor> cacheGet() {
        return cachedColors;
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
    public int add(String value, String perm, String label) {
        int databaseId = Queries.NameColor.saveColor(value, perm, label);
        if (databaseId != -1) {
            cacheAdd(
                new NameColorBuilder()
                    .withColor(value)
                    .withPerm(perm)
                    .withLabel(label)
                    .withDatabaseId(databaseId)
                    .createNameColor()
            );
            Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.COLOR, databaseId);
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
        Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.DELETE, SyncHandler.SyncType.COLOR, value.getDatabaseId());
        return true;
    }

    @Override
    public boolean update(String value, String perm, String label, int databaseId) {
        boolean success = Queries.NameColor.updateColor(value, perm, label, databaseId);
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
        Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.COLOR, databaseId);
        return true;
    }

    @Override
    public void load(NameColor nameColor) {
        cacheAdd(nameColor);
    }

    @Override
    public void loadAll() {
        Result<Record4<Integer, String, String, String>> result = Queries.NameColor.loadAllColors();

        if (result == null)
            return;

        for (Record4<Integer, String, String, String> record : result) {
            int databaseId = record.get(COLORS.ID);
            String color = record.get(COLORS.COLOR);
            String permission = record.get(COLORS.PERM);
            String label = record.get(COLORS.LABEL);

            cacheAdd(
                new NameColorBuilder()
                    .withColor(color)
                    .withPerm(permission)
                    .withLabel(label)
                    .withDatabaseId(databaseId)
                    .createNameColor()
            );
        }
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
        if (GuiCooldown.hasCooldown(uuid))
            return false;

        // Trying to set same value
        if (playerData.getNameColor().isPresent() && playerData.getNameColor().get().equals(nameColor))
            return false;

        GuiCooldown.setCooldown(uuid);

        final int databaseId = nameColor.getDatabaseId();
        playerData.setNameColor(nameColor);
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> Queries.NameColor.Players.savePlayerColor(uuid, databaseId));

        return true;
    }

    public HashMap<String, Integer> getColorKeys() {
        return colorKeys;
    }
}
