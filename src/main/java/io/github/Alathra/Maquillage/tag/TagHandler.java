package io.github.Alathra.Maquillage.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Result;

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
        Result<Record5<Integer, String, String, String, String>> result =  DatabaseQueries.loadAllTags();
        int index = 0;
        for (Record5 record : result) {
            loadedTags.put((int) result.getValue(index, "ID"),
                new Tag(
                    result.getValue(index, "TAG").toString(),
                    result.getValue(index, "PERM").toString(),
                    result.getValue(index, "DISPLAYNAME").toString(),
                    result.getValue(index, "IDENTIFIER").toString(),
                    (Integer) result.getValue(index, "ID"))
            );
            index ++;
        }
    }

    public static void clearTags() {
        loadedTags.clear();
    }

    /**
     * Attempts to save a tag to the database
     *
     * @param tag
     * @param perm
     * @param name
     * @return value of {@link DatabaseQueries#saveTag}
     */
    public static int addTagToDB(String tag, String perm, String name, String identifier) {
        return DatabaseQueries.saveTag(tag, perm, name, identifier);
    }

    /**
     * Caches a tag
     *
     * @param tag
     */
    public static void addTagToCache(Tag tag) {
        loadedTags.put(tag.getID(), tag);
    }

    /**
     * Attempts to save a tag to DB and, if successful, caches the tag
     *
     * @param tag
     * @param perm
     * @param name
     * @return value of {@link TagHandler#addTagToDB}
     */
    public static int addTag(String tag, String perm, String name, String identifier) {
        int ID = addTagToDB(tag, perm, name, identifier);
        if (ID != -1)
            addTagToCache(new Tag(tag, perm, name, identifier, ID));
        return ID;
    }

    public static boolean doesPlayerHaveTag (UUID uuid) {
        return playerTags.containsKey(uuid);
    }

    public static boolean doesPlayerHaveTag (Player p) {
        return doesPlayerHaveTag(p.getUniqueId());
    }

    public static String getPlayerTagString(UUID uuid) {
        String playerTagString = loadedTags.get(playerTags.get(uuid)).getTag();
        if (playerTagString == null)
            return "";
        return playerTagString;
    }

    public static String getPlayerTagString(Player p) {
        return getPlayerTagString(p.getUniqueId());
    }

    /**
     *
     * @param uuid
     * @return the player's selected Tag object
     */
    public static Tag getPlayerTag(UUID uuid) {
        return loadedTags.get(playerTags.get(uuid));
    }

    public static Tag getPlayerTag(Player p) {
        return getPlayerTag(p.getUniqueId());
    }

    /**
     *
     * @param uuid
     * @return the color ID for the player's selected color
     */
    public static int getPlayerTagID(UUID uuid) {
        return playerTags.get(uuid);
    }

    public static int getPlayerTagID(Player p) {
        return getPlayerTagID(p.getUniqueId());
    }

    public static Tag getTagByID (int tagID) {
        return loadedTags.get(tagID);
    }

    public static void setPlayerTag (Player p, Tag tag) {
        UUID uuid = p.getUniqueId();
        int tagID = tag.getID();
        playerTags.put(uuid, tagID);
        DatabaseQueries.savePlayerTag(uuid, tagID);

        Component name = ColorParser.of(tag.getTag() + p.getName()).build();
        p.displayName(name);
        p.playerListName(name);
    }

    public static void setPlayerTag (Player p, int tagID) {
        setPlayerTag(p, getTagByID(tagID));
    }

}
