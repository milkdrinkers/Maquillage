package io.github.Alathra.Maquillage.tag;

import io.github.Alathra.Maquillage.db.DatabaseQueries;
import org.bukkit.entity.Player;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class TagHandler {

    public static HashMap<UUID, Integer> playerTags = new HashMap<>();
    public static HashMap<Integer, Tag> loadedTags = new HashMap<>();

    public static void loadPlayerTag(UUID uuid) {
        Record1<Integer> record = DatabaseQueries.loadPlayerColor(uuid);
        if (record == null)
            return;
        int colorID = record.component1();
        playerTags.put(uuid, colorID);
    }

    public static void loadPlayerTag(Player p) {
        loadPlayerTag(p.getUniqueId());
    }

    public static void removePlayerTag(UUID uuid) {
        playerTags.remove(uuid);
    }

    public static void removePlayerTag(Player p) {
        removePlayerTag(p.getUniqueId());
    }

    /**
     * Removes a player's tag from cache and DB.
     *
     * @param uuid
     */
    public static void clearPlayerTag(UUID uuid) {
        removePlayerTag(uuid);
        DatabaseQueries.removePlayerTag(uuid);
    }

    public static void clearPlayerTag(Player p) {
        clearPlayerTag(p.getUniqueId());
    }

    public static void loadTags() {
        Result<Record4<Integer, String, String, String>> result =  DatabaseQueries.loadAllTags();
        int index = 0;
        for (Record4 record : result) {
            loadedTags.put((int) result.getValue(index, "ID"),
                new Tag(
                    result.getValue(index, "TAG").toString(),
                    result.getValue(index, "PERM").toString(),
                    result.getValue(index, "NAME").toString())
            );
            index ++;
        }
    }

    /**
     * Attempts to save a tag to the database, and if successful caches the tag
     *
     * @param tag
     * @return
     */
    public static int addTag(Tag tag) {
        int ID = DatabaseQueries.saveTag(tag);
        if (ID != -1)
            loadedTags.put(ID, tag);
        return ID;
    }

    public static boolean doesPlayerHaveTag (UUID uuid) {
        return playerTags.containsKey(uuid);
    }

    public static boolean doesPlayerHaveTag (Player p) {
        return doesPlayerHaveTag(p.getUniqueId());
    }

    public static String getPlayerTag (UUID uuid) {
        return loadedTags.get(playerTags.get(uuid)).getTag();
    }

    public static String getPlayerTag (Player p) {
        return getPlayerTag(p.getUniqueId());
    }

    public static Tag getTagByID (int tagID) {
        return loadedTags.get(tagID);
    }

    public static void setPlayerTag (UUID uuid, int tagID) {
        playerTags.put(uuid, tagID);
        DatabaseQueries.savePlayerColor(uuid, tagID);
    }

    public static void setPlayerTag (Player p, int tagID) {
        setPlayerTag(p.getUniqueId(), tagID);
    }

}
