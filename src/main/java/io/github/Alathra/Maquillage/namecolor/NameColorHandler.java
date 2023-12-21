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

    public HashMap<UUID, NameColor> playerColors;
    public HashMap<Integer, NameColor> loadedColors;

    public void loadPlayerColor(UUID uuid) {
        Record1<Integer> record = DatabaseQueries.loadPlayerColor(uuid);
        if (record == null)
            return;
        int colorID = record.component1();
        playerColors.put(uuid, loadedColors.get(colorID));
    }

    public void loadPlayerColor(Player p) {
        loadPlayerColor(p.getUniqueId());
    }

    public void removePlayerColor(UUID uuid) {
        playerColors.remove(uuid);
    }

    public void removePlayerColor(Player p) {
        removePlayerColor(p.getUniqueId());
    }

    public void loadColors() {
        Result<Record3<Integer, String, String>> result =  DatabaseQueries.loadAllColors();
        int index = 0;
        for (Record3 record : result) {
            loadedColors.put((int) result.getValue(index, "ID"),
                new NameColor(result.getValue(index, "COLOR").toString(), result.getValue(index, "PERM").toString()));
            index ++;
        }
    }

    public void addColor(String color, String perm) {
        DatabaseQueries.saveColor(color, perm);
        int nextID = Collections.max(loadedColors.keySet()) + 1;
        loadedColors.put(nextID, new NameColor(color, perm));
    }

}
