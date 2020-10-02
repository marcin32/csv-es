package com.marcin32.source.model.csv;

import java.util.Optional;

public interface ITableFormatAdapter<TARGET_TYPE> {
    Optional<TARGET_TYPE> convertCsvLine(final String[] csvLine);
}
