package io.github.alathra.maquillage.module;

public interface Identifiable {
    int getDatabaseId();

    void setDatabaseId(int databaseId);

    boolean equals(int id);
}
