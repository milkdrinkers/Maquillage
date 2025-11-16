package io.github.milkdrinkers.maquillage.utility;

import io.github.milkdrinkers.maquillage.hook.Hook;
import io.github.milkdrinkers.maquillage.module.cosmetic.namecolor.NameColorHolder;
import io.github.milkdrinkers.maquillage.module.cosmetic.tag.TagHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class PermissionUtility {

    /**
     * Checks if the given permission node is registered, and if not, registers it.
     *
     * @param permission
     */
    public static void registerPermission(String permission) {
        if (Bukkit.getPluginManager().getPermission(permission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(permission));
        }
    }

    /**
     * Checks if the permission node is in use by other objects, and if not, removes it.
     *
     * @param permission
     */
    public static void removePermission(String permission) {
        if (TagHolder.getInstance().cacheGet().values().stream().noneMatch(t -> t.getPerm().equals(permission))
            || NameColorHolder.getInstance().cacheGet().values().stream().noneMatch(c -> c.getPerm().equals(permission))) {
            Bukkit.getPluginManager().removePermission(permission);
        }
    }

    public static boolean playerHasPermission(Player p, String permission) {
        return Hook.getVaultHook().isHookLoaded() && Hook.getVaultHook().isPermissionsLoaded() && Hook.getVaultHook().getPermissions().has(p, permission);
    }

    /**
     * Sanitizes a string into a valid permission node.
     * @param permission permission
     * @return sanitized permission node
     */
    public static String sanitizePermission(final String permission) {
        String sanitized = permission;
        while (sanitized.charAt(0) == '.') {
            sanitized = sanitized.substring(1);
        }
        return sanitized;
    }
}
