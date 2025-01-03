package io.github.milkdrinkers.maquillage.module.cosmetic.tag;

import io.github.milkdrinkers.maquillage.module.cosmetic.BaseCosmetic;

public class Tag extends BaseCosmetic {
    private String tag;

    Tag(String tag, String perm, String label, String key, int databaseId) {
        super(perm, label, key, databaseId);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean equals(Tag otherTag) {
        return getDatabaseId() == otherTag.getDatabaseId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag otherTag)) return false;
        return getDatabaseId() == otherTag.getDatabaseId();
    }

    @Override
    public String toString() {
        return "Tag{" +
            "tag='" + getTag() + '\'' +
            ", perm='" + getPerm() + '\'' +
            ", label='" + getLabel() + '\'' +
            ", key='" + getKey() + '\'' +
            ", databaseId=" + getDatabaseId() +
            '}';
    }
}
