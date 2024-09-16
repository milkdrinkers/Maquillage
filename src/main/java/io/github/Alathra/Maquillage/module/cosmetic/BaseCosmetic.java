package io.github.alathra.maquillage.module.cosmetic;

import io.github.alathra.maquillage.Maquillage;
import io.github.alathra.maquillage.module.Identifiable;
import io.github.alathra.maquillage.module.Labelable;
import io.github.alathra.maquillage.module.Permissible;
import org.bukkit.entity.Player;

public abstract class BaseCosmetic implements Permissible, Labelable, CosmeticIdentifiable, Identifiable {
    private String perm;
    private String label;
    private String key;
    private int ID;

    public BaseCosmetic(String perm, String label, String key, int ID) {
        this.perm = perm;
        this.label = label;
        this.key = key;
        this.ID = ID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public boolean hasPerm(Player p) {
        return Maquillage.getVaultHook().getPermissions().has(p, this.getPerm());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean equals(int id) {
        return getID() == id;
    }

    public boolean equals(BaseCosmetic baseCosmetic) {
        return getID() == baseCosmetic.getID();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseCosmetic baseCosmetic)) return false;
        return getID() == baseCosmetic.getID();
    }

    @Override
    public String toString() {
        return "BaseCosmetic{" +
            "perm='" + perm + '\'' +
            ", name='" + label + '\'' +
            ", identifier='" + key + '\'' +
            ", ID=" + ID +
            '}';
    }
}
