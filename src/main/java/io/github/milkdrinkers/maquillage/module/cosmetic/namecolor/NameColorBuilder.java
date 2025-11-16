package io.github.milkdrinkers.maquillage.module.cosmetic.namecolor;

import io.github.milkdrinkers.maquillage.database.schema.tables.records.ColorsRecord;
import io.github.milkdrinkers.maquillage.utility.Util;
import org.jetbrains.annotations.Nullable;

public final class NameColorBuilder {
    private int id = -1;
    private @Nullable String label;
    private @Nullable String perm;
    private @Nullable Integer weight;
    private @Nullable String color;

    public NameColorBuilder withDatabaseId(int databaseId) {
        this.id = databaseId;
        return this;
    }

    public NameColorBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public NameColorBuilder withPerm(String perm) {
        if (!perm.isEmpty() && !perm.startsWith("maquillage.namecolor."))
            perm = "maquillage.namecolor." + perm;

        this.perm = perm;
        return this;
    }

    public NameColorBuilder withWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public NameColorBuilder withColor(String color) {
        this.color = color;
        return this;
    }


    public NameColor createNameColor() throws IllegalStateException {
        if (color == null)
            throw new IllegalStateException("Missing state color when creating NameColor object");

        if (perm == null)
            throw new IllegalStateException("Missing state perm when creating NameColor object");

        if (label == null)
            throw new IllegalStateException("Missing state label when creating NameColor object");

        if (weight == null)
            throw new IllegalStateException("Missing state weight when creating NameColor object");

        if (id == -1)
            throw new IllegalStateException("Missing state id when creating NameColor object");

        return new NameColor(
            id,
            label,
            perm,
            Util.createKey(label, NameColorHolder.getInstance().getColorKeys()),
            weight,
            color
        );
    }

    public static NameColor deserialize(ColorsRecord record) {
        return new NameColorBuilder()
            .withDatabaseId(record.getId())
            .withLabel(record.getLabel())
            .withPerm(record.getPerm())
            .withWeight(record.getWeight())
            .withColor(record.getColor())
            .createNameColor();
    }
}