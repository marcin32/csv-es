package com.marcin32.source.model;

public class PackedTableMetadata implements ITableMetadata {

    private final String className;
    private final long numberOfEntities;

    public PackedTableMetadata(final String className, final long numberOfEntities) {
        this.className = className;
        this.numberOfEntities = numberOfEntities;
    }

    @Override
    public String getFileName() {
        return className;
    }

    @Override
    public long getNumberOfEntities() {
        return numberOfEntities;
    }
}
