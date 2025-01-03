package io.github.milkdrinkers.maquillage.module.cosmetic.namecolor;

import io.github.milkdrinkers.maquillage.utility.Util;
import org.jetbrains.annotations.Nullable;

public class NameColorBuilder {
    private @Nullable String color;
    private @Nullable String perm;
    private @Nullable String label;
    private int id = -1;

    public NameColorBuilder withColor(String color) {
        this.color = color;
        return this;
    }

    public NameColorBuilder withPerm(String perm) {
        if (!perm.isEmpty() && !perm.startsWith("maquillage.namecolor."))
            perm = "maquillage.namecolor." + perm;

        this.perm = perm;
        return this;
    }

    public NameColorBuilder withLabel(String label) {
        this.label = label;
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

        if (label == null)
            throw new IllegalStateException("Missing state name when creating Tag object");

        if (id == -1)
            throw new IllegalStateException("Missing state id when creating Tag object");

        return new NameColor(
            color,
            perm,
            label,
            Util.createKey(label, NameColorHolder.getInstance().getColorKeys()),
            id
        );
    }
}