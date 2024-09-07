package io.github.Alathra.Maquillage.module.tag;

import io.github.Alathra.Maquillage.module.BaseCosmetic;

public class Tag extends BaseCosmetic {
    private String tag;

    Tag(String tag, String perm, String name, String identifier, int ID) {
        super(perm, name, identifier, ID);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean equals(Tag otherTag) {
        return getID() == otherTag.getID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag otherTag)) return false;
        return getID() == otherTag.getID();
    }

    @Override
    public String toString() {
        return "Tag{" +
            "tag='" + getTag() + '\'' +
            ", perm='" + getPerm() + '\'' +
            ", name='" + getName() + '\'' +
            ", identifier='" + getIdentifier() + '\'' +
            ", ID=" + getID() +
            '}';
    }
}
