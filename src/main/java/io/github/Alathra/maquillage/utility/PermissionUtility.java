package io.github.alathra.maquillage.utility;

import io.github.alathra.maquillage.module.namecolor.NameColorHolder;
import io.github.alathra.maquillage.module.tag.TagHolder;
import org.bukkit.Bukkit;
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
}
