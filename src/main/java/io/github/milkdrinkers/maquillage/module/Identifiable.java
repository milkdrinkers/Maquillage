package io.github.milkdrinkers.maquillage.module;

public interface Identifiable {
    int getDatabaseId();

    void setDatabaseId(int databaseId);

    boolean equals(int id);
}
