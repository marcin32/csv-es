package com.marcin32.source.model.csv;

import com.marcin32.source.base.Constants;
import com.marcin32.source.model.PackedTableMetadata;

import java.util.Optional;

public class MetadataAdapter extends AbstractFormatAdapter<PackedTableMetadata> {
    @Override
    public Optional<PackedTableMetadata> deserializeCsvLine(final String[] csvLine) {
        try {
            final String className = csvLine[0];
            final long numberOfLines = Long.parseLong(csvLine[1]);
            return Optional.of(new PackedTableMetadata(className, numberOfLines));
        } catch (final Throwable th) {
            System.err.println("Error parsing line from metadata file: " + String.join(" ", csvLine));
        }
        return Optional.empty();
    }

    @Override
    String serializeContentInternal(final String... parts) {
        return String.join(Constants.CSV_SEPARATOR_FOR_WRITING, parts);
    }

    @Override
    int getDesiredPartsLength() {
        return 1;
    }
}
