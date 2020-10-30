package com.marcin32.source.model;

public class SourceEntry<ENTITYTYPE> extends CsvEntry {

    private final ENTITYTYPE entity;

    public SourceEntry(final CsvEntry csvEntity, final ENTITYTYPE entity) {
        super(csvEntity.getTimestamp(), csvEntity.getUuid(),
                csvEntity.getShaContentHash(), csvEntity.getContent());
        this.entity = entity;
    }

    public SourceEntry(final Long timestamp, final String uuid, final String shaContentHash,
                       final String content, final ENTITYTYPE entity) {
        super(timestamp, uuid, shaContentHash, content);
        this.entity = entity;
    }

    public ENTITYTYPE getEntity() {
        return entity;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof SourceEntry)) return false;
        final SourceEntry<?> other = (SourceEntry<?>) o;
        if (!other.canEqual((Object) this)) return false;
        if (!super.equals(o)) return false;
        final Object this$entity = this.getEntity();
        final Object other$entity = other.getEntity();
        if (this$entity == null ? other$entity != null : !this$entity.equals(other$entity)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SourceEntry;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = super.hashCode();
        final Object $entity = this.getEntity();
        result = result * PRIME + ($entity == null ? 43 : $entity.hashCode());
        return result;
    }
}
