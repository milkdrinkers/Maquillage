package io.github.Alathra.Maquillage.listener.listeners;

import io.github.Alathra.Maquillage.namecolor.NameColorHandler;
import io.github.Alathra.Maquillage.tag.TagHandler;
import io.github.Alathra.Maquillage.utility.UpdateDisplayName;
import net.ess3.api.events.NickChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EssentialsNicknameListener implements Listener {

    @EventHandler
    public void onEssentialsNickname(NickChangeEvent event) {
        Player player = event.getAffected().getBase();

        String newName = event.getValue();
        if (newName != null) {
            UpdateDisplayName.updateDisplayNameOnNickChange(player, newName, TagHandler.getPlayerTag(player), NameColorHandler.getPlayerColor(player));
        } else {
            UpdateDisplayName.updateDisplayNameOnNickReset(player, TagHandler.getPlayerTag(player), NameColorHandler.getPlayerColor(player));
        }
    }
}
