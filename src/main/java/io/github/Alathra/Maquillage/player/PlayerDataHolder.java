package io.github.Alathra.Maquillage.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataHolder {
    private static PlayerDataHolder INSTANCE;
    private final HashMap<UUID, PlayerData> playerData = new HashMap<>();

    private PlayerDataHolder() {
    }

    public static PlayerDataHolder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PlayerDataHolder();

        return INSTANCE;
    }

    // SECTION Modifying

    @Nullable
    public PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    @Nullable
    public PlayerData getPlayerData(Player p) {
        return getPlayerData(p.getUniqueId());
    }

    public void setPlayerData(UUID uuid, PlayerData data) {
        playerData.put(uuid, data);
    }

    public void setPlayerData(Player p, PlayerData data) {
        setPlayerData(p.getUniqueId(), data);
    }

    public void removePlayerData(UUID uuid) {
        playerData.remove(uuid);
    }

    public void removePlayerData(Player p) {
        removePlayerData(p.getUniqueId());
    }

    public void clear() {
        playerData.clear();
    }

    public void clearNameColorWithId(int id) {
        playerData.forEach((uuid, data) -> {
            if (data.getNameColor().isPresent() && data.getNameColor().get().equals(id))
                data.clearNameColor();
        });
    }

    public void clearTagWithId(int id) {
        playerData.forEach((uuid, data) -> {
            if (data.getTag().isPresent() && data.getTag().get().equals(id))
                data.clearTag();
        });
    }

    // SECTION Checkers

    public boolean isPlayerDataLoaded(UUID uuid) {
        return playerData.containsKey(uuid);
    }

    public boolean isPlayerDataLoaded(Player p) {
        return isPlayerDataLoaded(p.getUniqueId());
    }
}
