package io.github.alathra.maquillage.module.cosmetic.tag;

import org.jetbrains.annotations.Nullable;

public class TagBuilder {
    private @Nullable String tag;
    private @Nullable String perm;
    private @Nullable String name;
    private @Nullable String identifier;
    private int id = -1;

    public TagBuilder withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public TagBuilder withPerm(String perm) {
        this.perm = perm;
        return this;
    }

    public TagBuilder withLabel(String label) {
        this.name = label;
        return this;
    }

    public TagBuilder withKey(String key) {
        this.identifier = key;
        return this;
    }

    public TagBuilder withDatabaseId(int databaseId) {
        this.id = databaseId;
        return this;
    }

    public Tag createTag() throws IllegalStateException {
        if (tag == null)
            throw new IllegalStateException("Missing state tag when creating Tag object");

        if (perm == null)
            throw new IllegalStateException("Missing state perm when creating Tag object");

        if (name == null)
            throw new IllegalStateException("Missing state name when creating Tag object");

        if (identifier == null)
            throw new IllegalStateException("Missing state identifier when creating Tag object");

        if (id == -1)
            throw new IllegalStateException("Missing state id when creating Tag object");

        return new Tag(
            tag,
            perm,
            name,
            identifier,
            id
        );
    }
}