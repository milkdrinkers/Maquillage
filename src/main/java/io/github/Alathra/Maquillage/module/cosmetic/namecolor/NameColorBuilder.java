package io.github.alathra.maquillage.module.cosmetic.namecolor;

import org.jetbrains.annotations.Nullable;

public class NameColorBuilder {
    private @Nullable String color;
    private @Nullable String perm;
    private @Nullable String name;
    private @Nullable String identifier;
    private int id = -1;

    public NameColorBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public NameColorBuilder withPerm(String perm) {
        this.perm = perm;
        return this;
    }

    public NameColorBuilder withLabel(String label) {
        this.name = label;
        return this;
    }

    public NameColorBuilder withKey(String key) {
        this.identifier = key;
        return this;
    }

    public NameColorBuilder withDatabaseId(int databaseId) {
        this.id = databaseId;
        return this;
    }

    public NameColor createNameColor() throws IllegalStateException {
        if (color == null)
            throw new IllegalStateException("Missing state color when creating Tag object");

        if (perm == null)
            throw new IllegalStateException("Missing state perm when creating Tag object");

        if (name == null)
            throw new IllegalStateException("Missing state name when creating Tag object");

        if (identifier == null)
            throw new IllegalStateException("Missing state identifier when creating Tag object");

        if (id == -1)
            throw new IllegalStateException("Missing state id when creating Tag object");

        return new NameColor(
            color,
            perm,
            name,
            identifier,
            id
        );
    }
}