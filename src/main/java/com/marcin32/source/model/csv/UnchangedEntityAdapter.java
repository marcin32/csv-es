package com.marcin32.source.model.csv;

import java.util.Optional;

public class UnchangedEntityAdapter implements ITableFormatAdapter<String> {
    @Override
    public Optional<String> convertCsvLine(final String[] csvLine) {
        try {
            return Optional.ofNullable(csvLine[0]);
        } catch (final Throwable th) {
            System.err.println("Error parsing line: " + String.join(" ", csvLine));
        }
        return Optional.empty();
    }
}
