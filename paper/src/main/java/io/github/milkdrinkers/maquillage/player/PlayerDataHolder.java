package io.github.milkdrinkers.maquillage.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataHolder {
    private static PlayerDataHolder INSTANCE;
    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();

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

    @NotNull
    public Optional<PlayerData> getPlayerDataOptional(Player p) {
        return Optional.ofNullable(getPlayerData(p));
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
