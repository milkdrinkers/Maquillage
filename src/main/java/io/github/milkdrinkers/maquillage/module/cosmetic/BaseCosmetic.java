package io.github.milkdrinkers.maquillage.module.cosmetic;

import io.github.milkdrinkers.maquillage.Maquillage;
import io.github.milkdrinkers.maquillage.module.Identifiable;
import io.github.milkdrinkers.maquillage.module.Labelable;
import io.github.milkdrinkers.maquillage.module.Permissible;
import org.bukkit.entity.Player;

public abstract class BaseCosmetic implements Permissible, Labelable, CosmeticIdentifiable, Identifiable {
    private String perm;
    private String label;
    private String key;
    private int databaseId;

    public BaseCosmetic(String perm, String label, String key, int databaseId) {
        this.perm = perm;
        this.label = label;
        this.key = key;
        this.databaseId = databaseId;
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

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public boolean equals(int id) {
        return getDatabaseId() == id;
    }

    public boolean equals(BaseCosmetic baseCosmetic) {
        return getDatabaseId() == baseCosmetic.getDatabaseId();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseCosmetic baseCosmetic)) return false;
        return getDatabaseId() == baseCosmetic.getDatabaseId();
    }

    @Override
    public String toString() {
        return "BaseCosmetic{" +
            "perm='" + perm + '\'' +
            ", name='" + label + '\'' +
            ", identifier='" + key + '\'' +
            ", ID=" + databaseId +
            '}';
    }
}
