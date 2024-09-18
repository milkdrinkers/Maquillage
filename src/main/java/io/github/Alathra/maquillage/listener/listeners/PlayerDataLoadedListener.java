package io.github.alathra.maquillage.listener.listeners;

import io.github.alathra.maquillage.event.PlayerDataLoadedEvent;
import io.github.alathra.maquillage.module.nickname.NicknameLookup;
import io.github.alathra.maquillage.utility.Cfg;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataLoadedListener implements Listener {

    @EventHandler
    public static void onPlayerDataLoaded(PlayerDataLoadedEvent e) {
        if (Cfg.get().getBoolean("module.nickname.set-displayname")) {
            String prefix = "";
            if (Cfg.get().getBoolean("module.nickname.prefix.enabled")){
                prefix = Cfg.get().getString("module.nickname.prefix.string");
            }

            e.getPlayer().displayName(Component.text(prefix + e.getData().getNicknameString()));
        }

        if (e.getData().getNicknameString() != null) {
            NicknameLookup.getInstance().addNicknameToLookup(e.getData().getNicknameString(), e.getPlayer());
        }
    }
}
