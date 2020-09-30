package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.dal.lowlevel.csv.CsvReader;
import com.marcin32.source.dal.lowlevel.file.FileReader;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.AbstractFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class TableReader extends AbstractTableReader {

    private final static CsvReader csvReader = new CsvReader();

    private final static FileReader fileReader = new FileReader();

    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                                     final Class<ENTITYTYPE> entitytype) throws IOException {

        return csvReader.readCsv(file)
                .map(entity -> deserializeEntity(entity, entitytype))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<String> readUuidsOfTimestampedEntities(final AbstractFile file) {

        return fileReader.readFile(file);
    }

    @Override
    public <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final String entityContentHash,
                                                                final Class<ENTITYTYPE> entity,
                                                                final AbstractFile file) {
        try {
            return readEntities(file, entity)
                    .anyMatch(entry -> entry.getShaContentHash().equals(entityContentHash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static <ENTITYTYPE> Optional<SourceEntry<ENTITYTYPE>> deserializeEntity(final CsvEntry abstractEntity,
                                                                            final Class<ENTITYTYPE> entitytype) {

        try {
            final ENTITYTYPE entity = gson.fromJson(abstractEntity.getContent(), entitytype);
            return Optional.of(new SourceEntry<>(abstractEntity, entity));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
