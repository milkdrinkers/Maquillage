package io.github.milkdrinkers.maquillage.module.cosmetic.tag;

import io.github.milkdrinkers.maquillage.database.schema.tables.records.TagsRecord;
import io.github.milkdrinkers.maquillage.utility.Util;
import org.jetbrains.annotations.Nullable;

public final class TagBuilder {
    private int id = -1;
    private @Nullable String label;
    private @Nullable String perm;
    private @Nullable Integer weight;
    private @Nullable String tag;

    public TagBuilder withDatabaseId(int databaseId) {
        this.id = databaseId;
        return this;
    }

    public TagBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public TagBuilder withPerm(String perm) {
        if (!perm.isEmpty() && !perm.startsWith("maquillage.tag."))
            perm = "maquillage.tag." + perm;

        this.perm = perm;
        return this;
    }

    public TagBuilder withWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public TagBuilder withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Tag createTag() throws IllegalStateException {
        if (tag == null)
            throw new IllegalStateException("Missing state tag when creating Tag object");

        if (perm == null)
            throw new IllegalStateException("Missing state perm when creating Tag object");

        if (label == null)
            throw new IllegalStateException("Missing state name when creating Tag object");

        if (weight == null)
            throw new IllegalStateException("Missing state weight when creating Tag object");

        if (id == -1)
            throw new IllegalStateException("Missing state id when creating Tag object");

        return new Tag(
            id,
            label,
            perm,
            Util.createKey(label, TagHolder.getInstance().getTagKeys()),
            weight,
            tag
        );
    }

    public static Tag deserialize(TagsRecord record) {
        return new TagBuilder()
            .withDatabaseId(record.getId())
            .withLabel(record.getLabel())
            .withPerm(record.getPerm())
            .withWeight(record.getWeight())
            .withTag(record.getTag())
            .createTag();
    }
}