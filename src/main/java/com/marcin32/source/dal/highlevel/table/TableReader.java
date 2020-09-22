package com.marcin32.source.dal.highlevel.table;

import com.google.gson.Gson;
import com.marcin32.source.dal.lowlevel.csv.CsvReader;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.AbstractFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class TableReader {

    private final static Gson gson = new Gson();

    private final static CsvReader csvReader = new CsvReader();

    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                                     final Class<ENTITYTYPE> entitytype) throws IOException {

        return csvReader.readCsv(file)
                .map(entity -> deserializeEntity(entity, entitytype))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    /*public <ENTITYTYPE> Stream<AbstractSourceEntity<ENTITYTYPE>> readEntities(final TarArchiveInputStream tarArchiveInputStream,
                                                                              final Class<ENTITYTYPE> entitytype) {

        return csvReader.readCsv(tarArchiveInputStream)
                .map(entity -> deserializeEntity(entity, entitytype))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }*/

    public static <ENTITYTYPE> Optional<SourceEntry<ENTITYTYPE>> deserializeEntity(final CsvEntry abstractEntity,
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
