package io.github.milkdrinkers.maquillage.command.nickname;

import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class NicknameCooldown {

    private static HashMap<UUID, Instant> cooldowns = new HashMap<>();

    public HashMap<UUID, Instant> getCooldowns() {
        return cooldowns;
    }

    public static boolean hasCooldown(UUID uuid) {
        Instant cooldown = cooldowns.get(uuid);
        if (cooldown == null) return false;
        return Instant.now().isBefore(cooldown);
    }

    public static boolean hasCooldown(Player p) {
        return hasCooldown(p.getUniqueId());
    }

    public static void setCooldown(UUID uuid) {
        cooldowns.put(uuid, Instant.now().plusSeconds(1));
    }

    public static void setCooldown(Player p) {
        setCooldown(p.getUniqueId());
    }
}
