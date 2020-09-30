package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.AbstractFile;

import java.io.IOException;
import java.util.stream.Stream;

public interface ITableReader {

    <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                              final Class<ENTITYTYPE> entitytype) throws IOException;

    Stream<String> readUuidsOfTimestampedEntities(final AbstractFile file);

    <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final ENTITYTYPE entity, final AbstractFile file);

    <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final String entityContentHash, final Class<ENTITYTYPE> entity,
                                                         final AbstractFile file);
}
