package io.github.Alathra.Maquillage.namecolor;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NameColorHandler {

    public static HashMap<UUID, Integer> playerColors = new HashMap<>();
    public static HashMap<Integer, NameColor> loadedColors = new HashMap<>();
    public static HashMap<String, Integer> colorIdentifiers = new HashMap<>();

    public static void loadPlayerColor(UUID uuid) {
        Record1<Integer> record = DatabaseQueries.loadPlayerColor(uuid);
        if (record == null)
            return;
        int colorID = record.component1();
        playerColors.put(uuid, colorID);
    }

    public static void loadPlayerColor(Player p) {
        loadPlayerColor(p.getUniqueId());
    }

    public static void removePlayerColor(UUID uuid) {
        playerColors.remove(uuid);
    }

    public static void removePlayerColor(Player p) {
        removePlayerColor(p.getUniqueId());
    }

    /**
     * Removes a player's color from cache and DB.
     *
     * @param uuid
     */
    public static void clearPlayerColor(UUID uuid) {
        removePlayerColor(uuid);
        DatabaseQueries.removePlayerColor(uuid);

        Player p = Bukkit.getPlayer(uuid);
        Component name = ColorParser.of("<white>" + p.getName()).build();
        p.displayName(name);
        p.playerListName(name);
    }

    public static void clearAllPlayerColorsWithID(int ID) {
        playerColors.values().removeAll(Collections.singleton(ID));
    }

    public static void clearPlayerColor(Player p) {
        clearPlayerColor(p.getUniqueId());
    }

    public static void loadColors() {
        Result<Record5<Integer, String, String, String, String>> result =  DatabaseQueries.loadAllColors();
        int index = 0;
        for (Record5 record : result) {
            int ID = (int) result.getValue(index, "ID");
            String identifier = result.getValue(index, "IDENTIFIER").toString();
            loadedColors.put((int) result.getValue(index, "ID"),
                new NameColor(
                    result.getValue(index, "COLOR").toString(),
                    result.getValue(index, "PERM").toString(),
                    result.getValue(index, "DISPLAYNAME").toString(),
                    identifier,
                    ID)
            );
            colorIdentifiers.put(identifier, ID);
            index ++;
        }
    }

    public static void clearColors() {
        loadedColors.clear();
    }

    /**
     * Attempts to save a color to the database
     *
     * @param color
     * @param perm
     * @param name
     * @return value of {@link DatabaseQueries#saveColor}
     */
    public static int addColorToDB(String color, String perm, String name, String identifier) {
        return DatabaseQueries.saveColor(color, perm, name, identifier);
    }

    /**
     * Caches a color
     *
     * @param color
     */
    public static void addColorToCache(NameColor color) {
        loadedColors.put(color.getID(), color);
        colorIdentifiers.put(color.getIdentifier(), color.getID());
    }

    /**
     * Attempts to save a color to DB and, if successful, caches the color
     * 
     * @param color
     * @param perm
     * @param name
     * @return value of {@link NameColorHandler#addColorToDB}
     */
    public static int addColor(String color, String perm, String name, String identifier) {
        int ID = addColorToDB(color, perm, name, identifier);
        if (ID != -1)
            addColorToCache(new NameColor(color, perm, name, identifier, ID));
        return ID;
    }

    public static boolean updateDBColor(String color, String perm, String name, String identifier, int ID) {
        return DatabaseQueries.updateColor(color, perm, name, identifier, ID);
    }

    public static boolean updateColor(String color, String perm, String name, String identifier, int ID) {
        boolean success = updateDBColor(color, perm, name, identifier, ID);
        if (!success)
            return false;
        addColorToCache(new NameColor(color, perm, name, identifier, ID));
        return true;
    }

    public static boolean removeColorFromDB(NameColor color) {
        return DatabaseQueries.removeColor(color.getID());
    }

    public static void uncacheColor(NameColor color) {
        loadedColors.remove(color.getID());
        colorIdentifiers.remove(color.getIdentifier());
    }

    public static boolean removeColor(NameColor color) {
        boolean success = removeColorFromDB(color);
        if (!success)
            return false;
        uncacheColor(color);
        return true;
    }

    public static boolean doesPlayerHaveColor (UUID uuid) {
        return playerColors.containsKey(uuid);
    }

    public static boolean doesPlayerHaveColor (Player p) {
        return doesPlayerHaveColor(p.getUniqueId());
    }

    /**
     *
     * @param uuid
     * @return Color as a string that can be parsed by Adventure
     */
    public static String getPlayerColorString(UUID uuid) {
        String playerColorString = loadedColors.get(playerColors.get(uuid)).getColor();
        if (playerColorString == null)
            return "<white>";
        return playerColorString;
    }

    public static String getPlayerColorString(Player p) {
        return getPlayerColorString(p.getUniqueId());
    }

    /**
     *
     * @param uuid
     * @return the player's selected NameColor object
     */
    public static NameColor getPlayerColor(UUID uuid) {
        return loadedColors.get(playerColors.get(uuid));
    }

    public static NameColor getPlayerColor(Player p) {
        return getPlayerColor(p.getUniqueId());
    }

    /**
     *
     * @param uuid
     * @return the color ID for the player's selected color
     */
    public static int getPlayerColorID(UUID uuid) {
        return playerColors.get(uuid);
    }

    public static int getPlayerColorID(Player p) {
        return getPlayerColorID(p.getUniqueId());
    }

    public static NameColor getNameColorByID (int colorID) {
        return loadedColors.get(colorID);
    }

    public static NameColor getNameColorByIDString (String identifier) {
        return getNameColorByID(colorIdentifiers.get(identifier));
    }

    public static List<String> getAllIdentifiers() {
        return colorIdentifiers.keySet().stream().toList();
    }

    public static boolean doesIdentifierExist(String identifier) {
        return colorIdentifiers.containsKey(identifier);
    }

    public static void setPlayerColor (Player p, NameColor color) {
        UUID uuid = p.getUniqueId();
        int colorID = color.getID();
        playerColors.put(uuid, colorID);
        DatabaseQueries.savePlayerColor(uuid, colorID);

        UpdateDisplayName.updateDisplayName(p, TagHandler.getPlayerTag(p), color);
    }

    public static void setPlayerColor (Player p, int colorID) {
        setPlayerColor(p, getNameColorByID(colorID));
    }
}
