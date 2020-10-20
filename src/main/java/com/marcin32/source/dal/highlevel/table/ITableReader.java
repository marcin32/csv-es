package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.ITableFormatAdapter;
import com.marcin32.source.model.file.AbstractFile;

import java.io.IOException;
import java.util.stream.Stream;

public interface ITableReader {

    <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                              final Class<ENTITYTYPE> entitytype) throws IOException;

    <TARGET_TYPE> Stream<TARGET_TYPE> readEntities(final AbstractFile file,
                                                   final ITableFormatAdapter<TARGET_TYPE> tableFormatAdapter);

    Stream<String> readUuidsOfTimestampedEntities(final AbstractFile file);

    <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final ENTITYTYPE entity, final AbstractFile file);

    boolean checkWhetherTableContainsHash(final String entityContentHash, final AbstractFile file);

    Stream<CsvEntry> readRawCsvEntries(final AbstractFile file);
}
