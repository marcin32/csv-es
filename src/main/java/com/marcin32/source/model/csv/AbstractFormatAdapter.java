package com.marcin32.source.model.csv;

public abstract class AbstractFormatAdapter<TARGET_TYPE> implements ITableFormatAdapter<TARGET_TYPE> {

    abstract String serializeContentInternal(final String... parts);

    abstract int getDesiredPartsLength();

    @Override
    public String serializeContent(final String... parts) {
        if (getDesiredPartsLength() != parts.length) {
            throw new IllegalArgumentException(String.format("Wrong number of arguments for parts serializer! Expected: {}, actual: {} ", getDesiredPartsLength(), parts.length));
        }
        return serializeContentInternal(parts);
    }
}
