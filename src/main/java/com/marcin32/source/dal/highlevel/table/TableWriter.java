package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.dal.lowlevel.csv.CsvWriter;
import com.marcin32.source.model.file.RawFile;

public class TableWriter extends TableReader {

    private final CsvWriter csvWriter = new CsvWriter();

    public <ENTITYTYPE> void writeEntity(final RawFile file,
                                         final String entityId,
                                         final ENTITYTYPE entity) {
        final String content = gson.toJson(entity);
        csvWriter.saveEntity(file, CHANGED_ENTITY_FORMAT_ADAPTER, entityId, content);
    }

    public <ENTITYTYPE> void writeTimestampForEntity(final RawFile file,
                                                     final String entityId) {
        csvWriter.saveEntity(file, UNCHANGED_ENTITY_FORMAT_ADAPTER, entityId);
    }

    public void closeTable(final RawFile rawFile) {
        csvWriter.closeCsvFile(rawFile);
    }
}
