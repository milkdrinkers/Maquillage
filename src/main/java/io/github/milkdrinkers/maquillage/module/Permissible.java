package io.github.milkdrinkers.maquillage.module;

import org.bukkit.entity.Player;

public interface Permissible {
    String getPerm();

    void setPerm(String perm);

    boolean hasPerm(Player p);
}
