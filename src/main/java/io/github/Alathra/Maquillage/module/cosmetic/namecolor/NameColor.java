package io.github.alathra.maquillage.module.cosmetic.namecolor;

import io.github.alathra.maquillage.module.cosmetic.BaseCosmetic;

public class NameColor extends BaseCosmetic {
    private String color;

    NameColor(String color, String perm, String name, String identifier, int ID) {
        super(perm, name, identifier, ID);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean equals(NameColor otherNameColor) {
        return getID() == otherNameColor.getID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameColor otherNameColor)) return false;
        return getID() == otherNameColor.getID();
    }

    @Override
    public String toString() {
        return "NameColor{" +
            "color='" + getColor() + '\'' +
            ", perm='" + getPerm() + '\'' +
            ", name='" + getName() + '\'' +
            ", identifier='" + getIdentifier() + '\'' +
            ", ID=" + getID() +
            '}';
    }
}
