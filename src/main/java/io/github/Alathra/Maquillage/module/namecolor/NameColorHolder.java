package io.github.Alathra.Maquillage.module.namecolor;

import io.github.Alathra.Maquillage.Maquillage;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.db.sync.SyncHandler;
import io.github.Alathra.Maquillage.gui.GuiCooldown;
import io.github.Alathra.Maquillage.module.BaseCosmeticHolder;
import io.github.Alathra.Maquillage.player.PlayerData;
import io.github.Alathra.Maquillage.player.PlayerDataHolder;
import io.github.Alathra.Maquillage.utility.PermissionUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.Record5;
import org.jooq.Result;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.github.Alathra.Maquillage.db.schema.tables.Colors.COLORS;

public class NameColorHolder implements BaseCosmeticHolder<NameColor> {
    private static NameColorHolder INSTANCE;
    private final HashMap<Integer, NameColor> cachedColors = new HashMap<>();
    private final HashMap<String, Integer> colorIdentifiers = new HashMap<>();

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
        cachedColors.put(color.getID(), color);
        colorIdentifiers.put(color.getIdentifier(), color.getID());

        PermissionUtility.registerPermission(color.getPerm());
    }

    @Override
    public void cacheRemove(NameColor value) {
        cachedColors.remove(value.getID());
        colorIdentifiers.remove(value.getIdentifier());

        PermissionUtility.removePermission(value.getPerm());
    }

    @Override
    public void cacheRemove(int id) {
        cacheRemove(getByID(id));
    }

    @Override
    public void cacheClear() {
        cachedColors.clear();
        colorIdentifiers.clear();
    }

    // SECTION Database

    @Override
    public int add(String value, String perm, String name, String identifier) {
        int ID = DatabaseQueries.saveColor(value, perm, name, identifier);
        if (ID != -1) {
            cacheAdd(
                new NameColorBuilder()
                    .withColor(value)
                    .withPerm(perm)
                    .withName(name)
                    .withIdentifier(identifier)
                    .withID(ID)
                    .createNameColor()
            );
            Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.COLOR, ID);
        }
        return ID;
    }

    @Override
    public boolean remove(NameColor value) {
        boolean success = DatabaseQueries.removeColor(value.getID());
        if (!success)
            return false;
        PlayerDataHolder.getInstance().clearNameColorWithId(value.getID());
        cacheRemove(value);
        Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.DELETE, SyncHandler.SyncType.COLOR, value.getID());
        return true;
    }

    @Override
    public boolean update(String value, String perm, String name, String identifier, int ID) {
        boolean success = DatabaseQueries.updateColor(value, perm, name, identifier, ID);
        if (!success)
            return false;

        cacheAdd(
            new NameColorBuilder()
                .withColor(value)
                .withPerm(perm)
                .withName(name)
                .withIdentifier(identifier)
                .withID(ID)
                .createNameColor()
        );
        Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.COLOR, ID);
        return true;
    }

    @Override
    public void load(int ID, String value, String perm, String name, String identifier) {
        cacheAdd(
            new NameColorBuilder()
                .withColor(value)
                .withPerm(perm)
                .withName(name)
                .withIdentifier(identifier)
                .withID(ID)
                .createNameColor()

        );
    }

    @Override
    public void loadAll() {
        Result<Record5<Integer, String, String, String, String>> result = DatabaseQueries.loadAllColors();

        if (result == null)
            return;

        for (Record5<Integer, String, String, String, String> record : result) {
            int ID = record.get(COLORS.ID);
            String color = record.get(COLORS.COLOR);
            String permission = record.get(COLORS.PERM);
            String name = record.get(COLORS.DISPLAYNAME);
            String identifier = record.get(COLORS.IDENTIFIER);

            cacheAdd(
                new NameColorBuilder()
                    .withColor(color)
                    .withPerm(permission)
                    .withName(name)
                    .withIdentifier(identifier)
                    .withID(ID)
                    .createNameColor()
            );
        }
    }

    // SECTION Identifiers

    @Override
    public NameColor getByID(int ID) {
        return cachedColors.get(ID);
    }

    @Override
    public NameColor getByIDString(String identifier) {
        return getByID(colorIdentifiers.get(identifier));
    }

    @Override
    public List<String> getAllIdentifiers() {
        return colorIdentifiers.keySet().stream().toList();
    }

    @Override
    public boolean doesIdentifierExist(String identifier) {
        return colorIdentifiers.containsKey(identifier);
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

        DatabaseQueries.removePlayerColor(uuid);
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

        final int colorID = nameColor.getID();
        playerData.setNameColor(nameColor);
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> DatabaseQueries.savePlayerColor(uuid, colorID));

        return true;
    }
}
