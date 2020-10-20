package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;

import java.util.stream.Stream;

public class PackageReaderWrapper {

    private final PackageReader packageReader = new PackageReader();

    private final PackageDescriptor packageDescriptor;

    public PackageReaderWrapper(final PackageDescriptor packageDescriptor) {
        this.packageDescriptor = packageDescriptor;
    }

    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entityClass) {
        return packageReader
                .readEntities(entityClass, packageDescriptor);
    }

    public <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entityClass) {
        return packageReader.readUuidsOfTimestampedEntities(entityClass, packageDescriptor);
    }

    public Stream<PackedTableMetadata> getPackageMetadata() {
        return packageReader.getPackageMetadata(packageDescriptor);
    }
}
