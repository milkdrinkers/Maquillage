package io.github.Alathra.Maquillage.namecolor;

import io.github.Alathra.Maquillage.db.DatabaseQueries;
import org.bukkit.entity.Player;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class NameColorHandler {

    public static HashMap<UUID, Integer> playerColors;
    public static HashMap<Integer, NameColor> loadedColors;

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

    public static void loadColors() {
        Result<Record3<Integer, String, String>> result =  DatabaseQueries.loadAllColors();
        int index = 0;
        for (Record3 record : result) {
            loadedColors.put((int) result.getValue(index, "ID"),
                new NameColor(result.getValue(index, "COLOR").toString(), result.getValue(index, "PERM").toString()));
            index ++;
        }
    }

    /**
     * Attempts to save a color to the database, and if successful caches the color
     *
     * @param color
     * @return value of {@link DatabaseQueries#saveColor}
     */
    public static int addColor(NameColor color) {
        int ID = DatabaseQueries.saveColor(color);
        if (ID != -1)
            loadedColors.put(ID, color);
        return ID;
    }

    public static boolean doesPlayerHaveColor (UUID uuid) {
        return playerColors.containsKey(uuid);
    }

    public static boolean doesPlayerHaveColor (Player p) {
        return doesPlayerHaveColor(p.getUniqueId());
    }

    public static String getPlayerColor (UUID uuid) {
        return loadedColors.get(playerColors.get(uuid)).getColor();
    }

    public static String getPlayerColor (Player p) {
        return getPlayerColor(p.getUniqueId());
    }

    public static NameColor getNameColorByID (int colorID) {
        return loadedColors.get(colorID);
    }

    public static void setPlayerColor (UUID uuid, int colorID) {
        playerColors.put(uuid, colorID);
        DatabaseQueries.savePlayerColor(uuid, colorID);
    }

    public static void setPlayerColor (Player p, int colorID) {
        setPlayerColor(p.getUniqueId(), colorID);
    }
}
