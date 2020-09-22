package com.marcin32.source.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
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
}
