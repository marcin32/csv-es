package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.ITableFormatAdapter;
import com.marcin32.source.model.file.AbstractFile;

import java.util.Optional;
import java.util.stream.Stream;

public class TableReader extends AbstractTableReader {

    @Override
    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                                     final Class<ENTITYTYPE> entitytype) {
        return readEntities(file, CHANGED_ENTITY_FORMAT_ADAPTER)
                .map(entity -> deserializeEntity(entity, entitytype))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    @Override
    public <TARGET_TYPE> Stream<TARGET_TYPE> readEntities(final AbstractFile file,
                                                          final ITableFormatAdapter<TARGET_TYPE> tableFormatAdapter) {
        return csvReader.readCsv(file)
                .map(tableFormatAdapter::deserializeCsvLine)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<String> readUuidsOfTimestampedEntities(final AbstractFile file) {

        return readEntities(file, UNCHANGED_ENTITY_FORMAT_ADAPTER);
    }

    @Override
    public boolean checkWhetherTableMightContainHash(final String entityContentHash,
                                                     final AbstractFile file) {
        return readRawCsvEntries(file)
                .anyMatch(entry -> entry.getShaContentHash().equals(entityContentHash));
    }

    @Override
    public void finalizedrdr() {

    }

    static <ENTITYTYPE> Optional<SourceEntry<ENTITYTYPE>> deserializeEntity(final CsvEntry abstractEntity,
                                                                            final Class<ENTITYTYPE> entitytype) {

        try {
            final ENTITYTYPE entity = gson.fromJson(abstractEntity.getContent(), entitytype);
            return Optional.of(new SourceEntry<>(abstractEntity, entity));
        } catch (Throwable ex) {
            System.err.println("Entity to deserialize: " + abstractEntity.getContent());
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
