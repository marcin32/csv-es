package com.marcin32.source.model.csv;

import java.util.Optional;

public interface ITableFormatAdapter<TARGET_TYPE> {
    Optional<TARGET_TYPE> deserializeCsvLine(final String[] csvLine);

    String serializeContent(final String... parts);

    String serializeContent(TARGET_TYPE entity);
}
