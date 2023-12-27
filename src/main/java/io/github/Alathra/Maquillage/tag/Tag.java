package io.github.Alathra.Maquillage.tag;

public class Tag {
    private String tag;
    private String perm;
    private String name;

    public Tag(String tag, String perm, String name) {
        this.tag = tag;
        this.perm = perm;
        this.name = name;
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
}
