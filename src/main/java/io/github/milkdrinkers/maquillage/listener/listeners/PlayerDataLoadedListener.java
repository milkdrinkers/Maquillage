package io.github.milkdrinkers.maquillage.listener.listeners;

import io.github.milkdrinkers.maquillage.event.PlayerDataLoadedEvent;
import io.github.milkdrinkers.maquillage.module.nickname.NicknameLookup;
import io.github.milkdrinkers.maquillage.player.PlayerData;
import io.github.milkdrinkers.maquillage.player.PlayerDataHolder;
import io.github.milkdrinkers.maquillage.utility.Cfg;
import io.github.milkdrinkers.maquillage.utility.PermissionUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataLoadedListener implements Listener {

    @EventHandler
    public static void onPlayerDataLoaded(PlayerDataLoadedEvent e) {
        PlayerData data = e.getData();

        if (Cfg.get().getBoolean("module.nickname.enabled")) {
            String prefix = "";
            if (Cfg.get().getBoolean("module.nickname.prefix.enabled")) {
                prefix = Cfg.get().getString("module.nickname.prefix.string");
            }

            if (e.getData().getNicknameString() != null && !e.getData().getNicknameString().isEmpty()) {
                if (Cfg.get().getBoolean("module.nickname.set-displayname"))
                    e.getPlayer().displayName(Component.text(prefix + e.getData().getNicknameString()));

                if (Cfg.get().getBoolean("module.nickname.set-listname"))
                    e.getPlayer().playerListName(Component.text(prefix + e.getData().getNicknameString()));

                NicknameLookup.getInstance().addNicknameToLookup(e.getData().getNicknameString(), e.getPlayer());
            }
        }

        if (Cfg.get().getBoolean("module.tag.enabled")) {
            if (e.getData().getTag().isPresent() &&
                !PermissionUtility.playerHasPermission(e.getPlayer(), e.getData().getTag().get().getPerm())) {
                data.clearTag();
            }
        }


        if (Cfg.get().getBoolean("module.namecolor.enabled")) {
            if (e.getData().getNameColor().isPresent() &&
                !PermissionUtility.playerHasPermission(e.getPlayer(), e.getData().getNameColor().get().getPerm())) {
                data.clearNameColor();
            }
        }

        PlayerDataHolder.getInstance().setPlayerData(e.getPlayer(), data);
    }

}
