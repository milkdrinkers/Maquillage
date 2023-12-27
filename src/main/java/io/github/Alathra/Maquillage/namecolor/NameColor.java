package io.github.Alathra.Maquillage.namecolor;

public class NameColor {
    private String color;
    private String perm;
    private String name;

    public NameColor(String color, String perm, String name) {
        this.color = color;
        this.perm = perm;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }
}
