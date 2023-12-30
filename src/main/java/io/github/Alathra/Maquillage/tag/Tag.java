package io.github.Alathra.Maquillage.tag;

import io.github.Alathra.Maquillage.Maquillage;
import org.bukkit.entity.Player;

public class Tag {
    private String tag;
    private String perm;
    private String name;
    private int ID;

    public Tag(String tag, String perm, String name, int ID) {
        this.tag = tag;
        this.perm = perm;
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
