package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.events.PlayerDataLoadedEvent;
import io.github.Alathra.Maquillage.module.namecolor.NameColorHolder;
import io.github.Alathra.Maquillage.module.tag.TagHolder;
import io.github.Alathra.Maquillage.player.PlayerData;
import io.github.Alathra.Maquillage.player.PlayerDataHolder;
import io.github.Alathra.Maquillage.utility.PermissionUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataLoadedListener implements Listener {

    @EventHandler
    public static void onPlayerDataLoaded(PlayerDataLoadedEvent e) {
        Player p = e.getPlayer();

        PlayerData data = PlayerDataHolder.getInstance().getPlayerData(p);

        if (data == null)
            return;

        if (data.getTag() != null
            && !PermissionUtility.checkPermission(p, data.getTag().get().getPerm())) {
            data.clearTag();
        }

        if (data.getNameColor() != null
            && !PermissionUtility.checkPermission(p, data.getNameColor().get().getPerm())) {
            data.clearNameColor();
        }

        PlayerDataHolder.getInstance().setPlayerData(p, data);
    }
}
