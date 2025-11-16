package io.github.milkdrinkers.maquillage.module.cosmetic.tag;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.cooldown.CooldownType;
import io.github.milkdrinkers.maquillage.cooldown.Cooldowns;
import io.github.milkdrinkers.maquillage.database.Queries;
import io.github.milkdrinkers.maquillage.database.schema.tables.records.TagsRecord;
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

public class TagHolder implements BaseCosmeticHolder<Tag> {
    private static TagHolder INSTANCE;
    private final Map<Integer, Tag> cachedTags = new ConcurrentHashMap<>();

    private final Map<String, Integer> tagKeys = new ConcurrentHashMap<>();

    private TagHolder() {
    }

    public static TagHolder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TagHolder();

        return INSTANCE;
    }

    // SECTION Cache

    @Override
    public Map<Integer, Tag> cacheGet() {
        return Collections.unmodifiableMap(cachedTags);
    }

    @Override
    public void cacheAdd(Tag tag) {
        cachedTags.put(tag.getDatabaseId(), tag);
        tagKeys.put(tag.getKey(), tag.getDatabaseId());

        PermissionUtility.registerPermission(tag.getPerm());
    }

    @Override
    public void cacheRemove(Tag value) {
        cachedTags.remove(value.getDatabaseId());
        tagKeys.remove(value.getKey());

        PermissionUtility.removePermission(value.getPerm());
    }

    @Override
    public void cacheRemove(int id) {
        cacheRemove(getByDatabaseId(id));
    }

    @Override
    public void cacheClear() {
        cachedTags.clear();
        tagKeys.clear();
    }

    // SECTION Database

    @Override
    public int add(String value, String perm, String label, int weight) {
        final int databaseId = Queries.Tag.saveTag(value, perm, label, weight);
        if (databaseId != -1) {
            cacheAdd(
                new TagBuilder()
                    .withTag(value)
                    .withPerm(perm)
                    .withLabel(label)
                    .withWeight(weight)
                    .withDatabaseId(databaseId)
                    .createTag()
            );
            MessagingUtils.sendTagFetch(databaseId);
        }
        return databaseId;
    }

    @Override
    public boolean remove(Tag value) {
        final boolean success = Queries.Tag.removeTag(value.getDatabaseId());
        if (!success)
            return false;
        PlayerDataHolder.getInstance().clearTagWithId(value.getDatabaseId());
        cacheRemove(value);
        MessagingUtils.sendTagDelete(value.getDatabaseId());
        return true;
    }

    @Override
    public boolean update(String value, String perm, String label, int databaseId, int weight) {
        final boolean success = Queries.Tag.updateTag(value, perm, label, databaseId, weight);
        if (!success)
            return false;

        cacheAdd(
            new TagBuilder()
                .withTag(value)
                .withPerm(perm)
                .withLabel(label)
                .withDatabaseId(databaseId)
                .withWeight(weight)
                .createTag()
        );
        MessagingUtils.sendTagFetch(databaseId);
        return true;
    }

    @Override
    public void load(Tag tag) {
        cacheAdd(tag);
    }

    @Override
    public void loadAll() {
        final Result<TagsRecord> result = Queries.Tag.loadAllTags();

        if (result == null)
            return;

        result.map(TagBuilder::deserialize)
            .forEach(this::cacheAdd);
    }

    // SECTION Identifiers

    @Override
    public Tag getByDatabaseId(int databaseId) {
        return cachedTags.get(databaseId);
    }

    @Override
    public Tag getByKey(String key) {
        return getByDatabaseId(tagKeys.get(key));
    }

    @Override
    public List<String> getAllKeys() {
        return tagKeys.keySet().stream().toList();
    }

    @Override
    public boolean doesKeyExist(String key) {
        return tagKeys.containsKey(key);
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

        Queries.Tag.Players.removePlayerTag(uuid);
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
        if (Cooldowns.has(p, CooldownType.Gui))
            return false;

        // Trying to set same value
        if (playerData.getTag().isPresent() && playerData.getTag().get().equals(tag))
            return false;

        Cooldowns.set(p, CooldownType.Gui, 2);

        final int tagID = tag.getDatabaseId();
        playerData.setTag(tag);
        Bukkit.getScheduler().runTaskAsynchronously(Maquillage.getInstance(), () -> Queries.Tag.Players.savePlayerTag(uuid, tagID));

        return true;
    }

    public Map<String, Integer> getTagKeys() {
        return Collections.unmodifiableMap(tagKeys);
    }
}
