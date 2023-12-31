package io.github.Alathra.Maquillage.namecolor;

import com.github.milkdrinkers.colorparser.ColorParser;
import io.github.Alathra.Maquillage.db.DatabaseQueries;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Result;

import java.util.HashMap;
import java.util.UUID;

public class NameColorHandler {

    public static HashMap<UUID, Integer> playerColors = new HashMap<>();
    public static HashMap<Integer, NameColor> loadedColors = new HashMap<>();

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

    public static void clearPlayerColor(Player p) {
        clearPlayerColor(p.getUniqueId());
    }

    public static void loadColors() {
        Result<Record4<Integer, String, String, String>> result =  DatabaseQueries.loadAllColors();
        int index = 0;
        for (Record4 record : result) {
            loadedColors.put((int) result.getValue(index, "ID"),
                new NameColor(
                    result.getValue(index, "COLOR").toString(),
                    result.getValue(index, "PERM").toString(),
                    result.getValue(index, "DISPLAYNAME").toString(),
                        (Integer) result.getValue(index, "ID"))
            );
            index ++;
        }
    }

    /**
     * Attempts to save a color to the database
     *
     * @param color
     * @param perm
     * @param name
     * @return value of {@link DatabaseQueries#saveColor}
     */
    public static int addColorToDB(String color, String perm, String name) {
        return DatabaseQueries.saveColor(color, perm, name);
    }

    /**
     * Caches a color
     *
     * @param color
     */
    public static void addColorToCache(NameColor color) {
        loadedColors.put(color.getID(), color);
    }

    /**
     * Attempts to save a color to DB and, if successful, caches the color
     * 
     * @param color
     * @param perm
     * @param name
     * @return value of {@link NameColorHandler#addColorToDB}
     */
    public static int addColor(String color, String perm, String name) {
        int ID = addColorToDB(color, perm, name);
        if (ID != -1)
            addColorToCache(new NameColor(color, perm, name, ID));
        return ID;
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
        return loadedColors.get(playerColors.get(uuid)).getColor();
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

    public static void setPlayerColor (Player p, NameColor color) {
        UUID uuid = p.getUniqueId();
        int colorID = color.getID();
        playerColors.put(uuid, colorID);
        DatabaseQueries.savePlayerColor(uuid, colorID);

        Component name = ColorParser.of(color.getColor() + p.getName()).build();
        p.displayName(name);
        p.playerListName(name);
    }

    public static void setPlayerColor (Player p, int colorID) {
        setPlayerColor(p, getNameColorByID(colorID));
    }
}
