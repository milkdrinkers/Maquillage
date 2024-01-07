package io.github.Alathra.Maquillage.namecolor;

import io.github.Alathra.Maquillage.Maquillage;
import org.bukkit.entity.Player;

public class NameColor {
    private String color;
    private String perm;
    private String name;
    private String identifier;
    private int ID;

    public NameColor(String color, String perm, String name, String identifier, int ID) {
        this.color = color;
        this.perm = perm;
        this.name = name;
        this.identifier = identifier;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public boolean hasPerm(Player p) {
        return Maquillage.getVaultHook().getVault().has(p, this.getPerm());
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
