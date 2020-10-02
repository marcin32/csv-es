package com.marcin32.source.model.csv;

import com.marcin32.source.model.PackedTableMetadata;

import java.util.Optional;

public class MetadataAdapter implements ITableFormatAdapter<PackedTableMetadata> {
    @Override
    public Optional<PackedTableMetadata> convertCsvLine(final String[] csvLine) {
        try {
            final String className = csvLine[0];
            final long numberOfLines = Long.parseLong(csvLine[1]);
            return Optional.of(new PackedTableMetadata(className, numberOfLines));
        } catch (final Throwable th) {
            System.err.println("Error parsing line: " + String.join(" ", csvLine));
        }
        return Optional.empty();
    }
}
