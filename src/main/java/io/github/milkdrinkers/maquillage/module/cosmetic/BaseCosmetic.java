package io.github.milkdrinkers.maquillage.module.cosmetic;

import io.github.milkdrinkers.maquillage.hook.Hook;
import io.github.milkdrinkers.maquillage.module.Identifiable;
import io.github.milkdrinkers.maquillage.module.Labelable;
import io.github.milkdrinkers.maquillage.module.Permissible;
import org.bukkit.entity.Player;

import java.util.Objects;

public abstract class BaseCosmetic implements Permissible, Labelable, CosmeticIdentifiable, Identifiable {
    private int databaseId;
    private String label;
    private String perm;
    private String key;
    private int weight;

    public BaseCosmetic(int databaseId, String label, String perm, String key, int weight) {
        this.databaseId = databaseId;
        this.label = label;
        this.perm = perm;
        this.key = key;
        this.weight = weight;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
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
        return Hook.Vault.isLoaded() && Hook.getVaultHook().isPermissionsLoaded() && Hook.getVaultHook().getPermissions().has(p, this.getPerm());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean equals(int id) {
        return getDatabaseId() == id;
    }

    public boolean equals(BaseCosmetic baseCosmetic) {
        return getDatabaseId() == baseCosmetic.getDatabaseId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseCosmetic baseCosmetic)) return false;
        return getDatabaseId() == baseCosmetic.getDatabaseId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPerm(), getLabel(), getKey(), getDatabaseId());
    }

    @Override
    public String toString() {
        return "BaseCosmetic{" +
            "perm='" + perm + '\'' +
            ", name='" + label + '\'' +
            ", identifier='" + key + '\'' +
            ", ID=" + databaseId +
            ", weight=" + weight +
            '}';
    }
}
