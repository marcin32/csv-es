package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;

import java.io.IOException;
import java.util.stream.Stream;

public interface IPackageDal {

    <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entitytype, final PackageDescriptor packageDescriptor) throws IOException;

    <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entitytype, final PackageDescriptor packageDescriptor);

    <ENTITYTYPE> boolean checkWhetherPackageContainsEntity(final ENTITYTYPE entity, final PackageDescriptor packageDescriptor);

    <ENTITYTYPE> long numberOfEntities(Class<ENTITYTYPE> entity, final PackageDescriptor packageDescriptor);

    Stream<PackedTableMetadata> getPackageMetadata(final PackageDescriptor packageDescriptor);
}
