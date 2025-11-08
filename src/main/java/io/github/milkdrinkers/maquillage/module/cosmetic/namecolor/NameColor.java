package io.github.milkdrinkers.maquillage.module.cosmetic.namecolor;

import io.github.milkdrinkers.maquillage.module.cosmetic.BaseCosmetic;

public class NameColor extends BaseCosmetic {
    private String color;

    NameColor(String color, String perm, String label, String key, int databaseId, int weight) {
        super(perm, label, key, databaseId, weight);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean equals(NameColor otherNameColor) {
        return getDatabaseId() == otherNameColor.getDatabaseId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameColor otherNameColor)) return false;
        return getDatabaseId() == otherNameColor.getDatabaseId();
    }

    @Override
    public int hashCode() {
        return getColor().hashCode();
    }

    @Override
    public String toString() {
        return "NameColor{" +
            "color='" + getColor() + '\'' +
            ", perm='" + getPerm() + '\'' +
            ", label='" + getLabel() + '\'' +
            ", key='" + getKey() + '\'' +
            ", databaseId=" + getDatabaseId() +
            ", weight=" + getWeight() +
            '}';
    }
}
