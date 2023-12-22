package io.github.Alathra.Maquillage.tag;

public class Tag {
    private String tag;
    private String perm;

    public Tag(String tag, String perm) {
        this.tag = tag;
        this.perm = perm;
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
}
