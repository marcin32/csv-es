package com.marcin32.source.model.csv;

import com.marcin32.source.model.CsvEntry;

import java.util.Optional;

public class ChangedEntityAdapter implements ITableFormatAdapter<CsvEntry> {
    @Override
    public Optional<CsvEntry> convertCsvLine(final String[] line) {

        final String entityUuid = line[0];
        final Long timeStamp = Long.parseLong(line[1]);
        final String entityContentShaHash = line[2];
        final String entityContent = line[3];
        try {
            return Optional.of(new CsvEntry(timeStamp, entityUuid, entityContentShaHash, entityContent));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
