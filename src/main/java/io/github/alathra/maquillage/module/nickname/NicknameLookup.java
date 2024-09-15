package io.github.alathra.maquillage.module.nickname;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NicknameLookup {

    private static NicknameLookup INSTANCE;
    private final HashMap<String, UUID> nicknameLookupMap = new HashMap<>();

    public NicknameLookup() {
    }

    public static NicknameLookup getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NicknameLookup();

        return INSTANCE;
    }

    public void addNicknameToLookup(String nickname, UUID uuid) {
        nicknameLookupMap.put(nickname, uuid);
    }

    public void addNicknameToLookup(String nickname, Player player) {
        addNicknameToLookup(nickname, player.getUniqueId());
    }

    public void removeNicknameFromLookup(String nickname) {
        nicknameLookupMap.remove(nickname);
    }

    public void clearNicknameLookup() {
        nicknameLookupMap.clear();
    }

    public UUID findUUIDFromNickname(String nickname) {
        return nicknameLookupMap.get(nickname);
    }

    public String findNameFromNickname(String nickname) {
        return Bukkit.getOfflinePlayer(nicknameLookupMap.get(nickname)).getName();
    }

}
