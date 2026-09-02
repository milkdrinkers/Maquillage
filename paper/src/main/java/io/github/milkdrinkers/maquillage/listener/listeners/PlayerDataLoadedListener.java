package io.github.milkdrinkers.maquillage.listener.listeners;

import io.github.milkdrinkers.maquillage.event.PlayerDataLoadedEvent;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import io.github.milkdrinkers.maquillage.utility.PermissionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataLoadedListener implements Listener {

    @EventHandler
    public static void onPlayerDataLoaded(PlayerDataLoadedEvent e) {
        PlayerData data = e.getData();

        if (Cfg.get().module.nickname.enabled) {
            String prefix = "";
            if (Cfg.get().module.nickname.prefix.enabled) {
                prefix = Cfg.get().module.nickname.prefix.string;
            }

            if (e.getData().getNicknameString() != null && !e.getData().getNicknameString().isEmpty()) {
                if (Cfg.get().module.nickname.setDisplayname)
                    e.getPlayer().displayName(Component.text(prefix + e.getData().getNicknameString()));

                if (Cfg.get().module.nickname.setListname)
                    e.getPlayer().playerListName(Component.text(prefix + e.getData().getNicknameString()));
            }
        }

        if (Cfg.get().module.tag.enabled) {
            if (e.getData().getTag().isPresent() &&
                !PermissionUtil.playerHasPermission(e.getPlayer(), e.getData().getTag().get().getPerm())) {
                data.clearTag();
            }
        }


        if (Cfg.get().module.namecolor.enabled) {
            if (e.getData().getNameColor().isPresent() &&
                !PermissionUtil.playerHasPermission(e.getPlayer(), e.getData().getNameColor().get().getPerm())) {
                data.clearNameColor();
            }
        }

        PlayerDataHolder.getInstance().setPlayerData(e.getPlayer(), data);
    }

}
