package io.github.alathra.maquillage.module;

import io.github.alathra.maquillage.Maquillage;
import org.bukkit.entity.Player;

public abstract class BaseCosmetic implements Permissible, Nameable, CosmeticIdentifiable, Identifiable {
    private String perm;
    private String name;
    private String identifier;
    private int ID;

    public BaseCosmetic(String perm, String name, String identifier, int ID) {
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

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public boolean hasPerm(Player p) {
        return Maquillage.getVaultHook().getPermissions().has(p, this.getPerm());
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
            ", name='" + name + '\'' +
            ", identifier='" + identifier + '\'' +
            ", ID=" + ID +
            '}';
    }
}
