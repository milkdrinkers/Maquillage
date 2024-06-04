package io.github.Alathra.Maquillage.tag;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.gui.GuiCooldown;
import io.github.Alathra.Maquillage.namecolor.NameColor;
import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Result;

import java.util.*;

public class TagHandler {

    public static HashMap<UUID, Integer> playerTags = new HashMap<>();
    public static HashMap<Integer, Tag> loadedTags = new HashMap<>();
    public static HashMap<String, Integer> tagIdentifiers = new HashMap<>();

    public static void loadPlayerTag(UUID uuid) {
        Record1<Integer> record = DatabaseQueries.loadPlayerTag(uuid);
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

    public static void clearALlPlayerTagsWithID(int ID) {
        playerTags.values().removeAll(Collections.singleton(ID));
    }

    public static void loadTags() {
        Result<Record5<Integer, String, String, String, String>> result =  DatabaseQueries.loadAllTags();
        int index = 0;
        for (Record5 record : result) {
            int ID = (int) result.getValue(index, "ID");
            String identifier = result.getValue(index, "IDENTIFIER").toString();
            loadedTags.put(ID,
                new Tag(
                    result.getValue(index, "TAG").toString(),
                    result.getValue(index, "PERM").toString(),
                    result.getValue(index, "DISPLAYNAME").toString(),
                    identifier,
                    ID)
            );
            tagIdentifiers.put(identifier, ID);
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
        tagIdentifiers.put(tag.getIdentifier(), tag.getID());
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

    public static boolean updateDBTag(String tag, String perm, String name, String identifier, int ID) {
        return DatabaseQueries.updateTag(tag, perm, name, identifier, ID);
    }

    public static boolean updateTag(String tag, String perm, String name, String identifier, int ID) {
        boolean success = updateDBTag(tag, perm, name, identifier, ID);
        if (!success)
            return false;
        addTagToCache(new Tag(tag, perm, name, identifier, ID));
        return true;
    }

    public static boolean removeTagFromDB(Tag tag) {
        return DatabaseQueries.removeTag(tag.getID());
    }

    public static void uncacheTag(Tag tag) {
        loadedTags.remove(tag.getID());
        tagIdentifiers.remove(tag.getIdentifier());
    }

    public static boolean removeTag(Tag tag) {
        boolean success = removeTagFromDB(tag);
        if (!success)
            return false;
        uncacheTag(tag);
        return true;
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

    public static Tag getTagByIDString (String identifier) {
        return getTagByID(tagIdentifiers.get(identifier));
    }

    public static List<String> getAllIdentifiers() {
        return tagIdentifiers.keySet().stream().toList();
    }

    public static boolean doesIdentifierExist(String identifier) {
        return tagIdentifiers.containsKey(identifier);
    }

    public static boolean setPlayerTag (Player p, Tag tag) {
        UUID uuid = p.getUniqueId();

        if (GuiCooldown.hasCooldown(uuid)) return false;

        int tagID = tag.getID();
        playerTags.put(uuid, tagID);
        DatabaseQueries.savePlayerTag(uuid, tagID);

        UpdateDisplayName.updateDisplayName(p, tag, NameColorHandler.getPlayerColor(p));
        return true;
    }

    public static void setPlayerTag (Player p, int tagID) {
        setPlayerTag(p, getTagByID(tagID));
    }

}
