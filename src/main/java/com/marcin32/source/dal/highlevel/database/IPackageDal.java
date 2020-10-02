package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;

import java.io.IOException;
import java.util.stream.Stream;

public interface IPackageDal {

    <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entitytype) throws IOException;

    <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entitytype);

    <ENTITYTYPE> boolean checkWhetherPackageContainsEntity(final ENTITYTYPE entity);

    <ENTITYTYPE> boolean numberOfEntities(Class<ENTITYTYPE> entity);

    Stream<PackedTableMetadata> listTables();
}
