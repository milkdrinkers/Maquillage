package io.github.alathra.maquillage.module.cosmetic.tag;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.database.DatabaseQueries;
import io.github.alathra.maquillage.database.sync.SyncHandler;
import io.github.alathra.maquillage.gui.GuiCooldown;
import io.github.alathra.maquillage.module.cosmetic.BaseCosmeticHolder;
import io.github.alathra.maquillage.player.PlayerData;
import io.github.alathra.maquillage.player.PlayerDataHolder;
import io.github.alathra.maquillage.utility.PermissionUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.Record5;
import org.jooq.Result;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.github.alathra.maquillage.database.schema.tables.Tags.TAGS;

public class TagHolder implements BaseCosmeticHolder<Tag> {
    private static TagHolder INSTANCE;
    private final HashMap<Integer, Tag> cachedTags = new HashMap<>();
    private final HashMap<String, Integer> tagKeys = new HashMap<>();

    private TagHolder() {
    }

    public static TagHolder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TagHolder();

        return INSTANCE;
    }

    // SECTION Cache

    @Override
    public HashMap<Integer, Tag> cacheGet() {
        return cachedTags;
    }

    @Override
    public void cacheAdd(Tag tag) {
        cachedTags.put(tag.getID(), tag);
        tagKeys.put(tag.getKey(), tag.getID());

        PermissionUtility.registerPermission(tag.getPerm());
    }

    @Override
    public void cacheRemove(Tag value) {
        cachedTags.remove(value.getID());
        tagKeys.remove(value.getKey());

        PermissionUtility.removePermission(value.getPerm());
    }

    @Override
    public void cacheRemove(int id) {
        cacheRemove(getByID(id));
    }

    @Override
    public void cacheClear() {
        cachedTags.clear();
        tagKeys.clear();
    }

    // SECTION Database

    @Override
    public int add(String value, String perm, String name, String identifier) {
        int ID = DatabaseQueries.saveTag(value, perm, name, identifier);
        if (ID != -1) {
            cacheAdd(
                new TagBuilder()
                    .withTag(value)
                    .withPerm(perm)
                    .withName(name)
                    .withIdentifier(identifier)
                    .withID(ID)
                    .createTag()
            );
            Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.TAG, ID);
        }
        return ID;
    }

    @Override
    public boolean remove(Tag value) {
        boolean success = DatabaseQueries.removeTag(value.getID());
        if (!success)
            return false;
        PlayerDataHolder.getInstance().clearTagWithId(value.getID());
        cacheRemove(value);
        Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.DELETE, SyncHandler.SyncType.TAG, value.getID());
        return true;
    }

    @Override
    public boolean update(String value, String perm, String name, String identifier, int ID) {
        boolean success = DatabaseQueries.updateTag(value, perm, name, identifier, ID);
        if (!success)
            return false;

        cacheAdd(
            new TagBuilder()
                .withTag(value)
                .withPerm(perm)
                .withName(name)
                .withIdentifier(identifier)
                .withID(ID)
                .createTag()
        );
        Maquillage.getSyncHandler().saveSyncMessage(SyncHandler.SyncAction.FETCH, SyncHandler.SyncType.TAG, ID);
        return true;
    }

    @Override
    public void load(Tag tag) {
        cacheAdd(tag);
    }

    @Override
    public void loadAll() {
        Result<Record5<Integer, String, String, String, String>> result = DatabaseQueries.loadAllTags();

        if (result == null)
            return;

        for (Record5<Integer, String, String, String, String> record : result) {
            int ID = record.get(TAGS.ID);
            String tag = record.get(TAGS.TAG);
            String permission = record.get(TAGS.PERM);
            String name = record.get(TAGS.DISPLAYNAME);
            String identifier = record.get(TAGS.IDENTIFIER);

            cacheAdd(
                new TagBuilder()
                    .withTag(tag)
                    .withPerm(permission)
                    .withName(name)
                    .withIdentifier(identifier)
                    .withID(ID)
                    .createTag()
            );
        }
    }

    // SECTION Identifiers

    @Override
    public Tag getByID(int ID) {
        return cachedTags.get(ID);
    }

    @Override
    public Tag getByIDString(String identifier) {
        return getByID(tagKeys.get(identifier));
    }

    @Override
    public List<String> getAllKeys() {
        return tagKeys.keySet().stream().toList();
    }

    @Override
    public boolean doesIdentifierExist(String identifier) {
        return tagKeys.containsKey(identifier);
    }

    // SECTION Player

    /**
     * Removes a player's tag from cache and DB.
     *
     * @param uuid uuid
     */
    public static void clearPlayerTag(UUID uuid) {
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(uuid);
        if (playerData != null)
            playerData.clearTag();

        DatabaseQueries.removePlayerTag(uuid);
    }

    public static void clearPlayerTag(Player p) {
        clearPlayerTag(p.getUniqueId());
    }

    public static boolean setPlayerTag(Player p, Tag tag) {
        UUID uuid = p.getUniqueId();
        PlayerData playerData = PlayerDataHolder.getInstance().getPlayerData(uuid);
        if (playerData == null)
            return false;

        // Has cooldown
        if (GuiCooldown.hasCooldown(uuid))
            return false;

        // Trying to set same value
        if (playerData.getTag().isPresent() && playerData.getTag().get().equals(tag))
            return false;

        GuiCooldown.setCooldown(uuid);

        final int tagID = tag.getID();
        playerData.setTag(tag);
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> DatabaseQueries.savePlayerTag(uuid, tagID));

        return true;
    }
}
