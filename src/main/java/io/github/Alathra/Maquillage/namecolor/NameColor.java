package io.github.Alathra.Maquillage.namecolor;

public class NameColor {
    private String color;
    private String perm;

    public NameColor(String color, String perm) {
        this.color = color;
        this.perm = perm;
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
