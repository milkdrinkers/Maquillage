package io.github.milkdrinkers.maquillage.cooldown;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Singleton to store and access player cooldowns.
 */
public final class Cooldown {
    private static Cooldown instance;

    @NotNull
    public static Cooldown getInstance() {
        if (instance == null) instance = new Cooldown();
        return instance;
    }

    private final Table<UUID, CooldownType, Instant> cooldowns = Tables.synchronizedTable(HashBasedTable.create());

    // Set cooldown
    @Nullable
    public Instant setCooldown(UUID uuid, CooldownType type, Instant instant) {
        return cooldowns.put(uuid, type, instant);
    }

    @Nullable
    public Instant setCooldown(UUID uuid, CooldownType type, Duration duration) {
        return setCooldown(uuid, type, Instant.now().plus(duration));
    }

    @Nullable
    public Instant setCooldown(UUID uuid, CooldownType type, int seconds) {
        final Duration duration = Duration.ofSeconds(seconds);
        return setCooldown(uuid, type, Instant.now().plus(duration));
    }

    @Nullable
    public Instant setCooldown(OfflinePlayer p, CooldownType type, Instant instant) {
        return setCooldown(p.getUniqueId(), type, instant);
    }

    @Nullable
    public Instant setCooldown(OfflinePlayer p, CooldownType type, Duration duration) {
        return setCooldown(p.getUniqueId(), type, Instant.now().plus(duration));
    }

    @Nullable
    public Instant setCooldown(OfflinePlayer p, CooldownType type, int seconds) {
        final Duration duration = Duration.ofSeconds(seconds);
        return setCooldown(p.getUniqueId(), type, Instant.now().plus(duration));
    }

    public boolean hasCooldown(UUID uuid, CooldownType type) {
        final @Nullable Instant cooldown = cooldowns.get(uuid, type);
        return cooldown != null && Instant.now().isBefore(cooldown);
    }

    public boolean hasCooldown(OfflinePlayer p, CooldownType type) {
        return hasCooldown(p.getUniqueId(), type);
    }

    @Nullable
    public Instant clearCooldown(UUID uuid, CooldownType type) {
        return cooldowns.remove(uuid, type);
    }

    @Nullable
    public Instant clearCooldown(OfflinePlayer p, CooldownType type) {
        return clearCooldown(p.getUniqueId(), type);
    }

    public void clearCooldowns(UUID uuid) {
        cooldowns.row(uuid).clear();
    }

    public void clearCooldowns(OfflinePlayer p) {
        clearCooldowns(p.getUniqueId());
    }

    @Nullable
    public Instant getCooldown(UUID uuid, CooldownType type) {
        return cooldowns.get(uuid, type);
    }

    @Nullable
    public Instant getCooldown(OfflinePlayer p, CooldownType type) {
        return getCooldown(p.getUniqueId(), type);
    }

    public Duration getRemainingCooldown(UUID uuid, CooldownType type) {
        final Instant cooldown = cooldowns.get(uuid, type);
        final Instant now = Instant.now();
        if (cooldown != null && now.isBefore(cooldown)) {
            return Duration.between(now, cooldown);
        } else {
            clearCooldown(uuid, type);
            return Duration.ZERO;
        }
    }

    public Duration getRemainingCooldown(Player p, CooldownType type) {
        return getRemainingCooldown(p.getUniqueId(), type);
    }

    public String getRemainingCooldownString(Player p, CooldownType type) {
        final Duration cooldownTime = getRemainingCooldown(p, type);
        final long hours = cooldownTime.toHoursPart();
        final long minutes = cooldownTime.toMinutesPart();
        final long seconds = cooldownTime.toSecondsPart();

        if (hours < 1) {
            if (minutes < 1) {
                return seconds + " seconds";
            }
            return minutes + " minutes and " + seconds + " seconds";
        }
        return hours + " hours, " + minutes + " minutes and " + seconds + " seconds";
    }

    public void reset() {
        instance = new Cooldown();
    }
}
