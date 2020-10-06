package com.marcin32.source.model.csv;

import java.util.Optional;

public class UnchangedEntityAdapter extends AbstractFormatAdapter<String> {
    @Override
    public Optional<String> deserializeCsvLine(final String[] csvLine) {
        try {
            return Optional.ofNullable(csvLine[0]);
        } catch (final Throwable th) {
            System.err.println("Error parsing line: " + String.join(" ", csvLine));
        }
        return Optional.empty();
    }

    @Override
    String serializeContentInternal(final String... parts) {
        return String.join("", parts);
    }

    @Override
    int getDesiredPartsLength() {
        return 1;
    }
}
