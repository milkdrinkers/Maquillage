package io.github.alathra.maquillage.module;

public interface Identifiable {
    int getID();

    void setID(int ID);

    boolean equals(int id);
}
