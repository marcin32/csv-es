package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.PackageScope;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;

import java.util.stream.Stream;

public class PackageReaderWrapper {

    private final PackageReader packageReader = new PackageReader();

    private final PackageDescriptor packageDescriptor;

    PackageReaderWrapper(final PackageDescriptor packageDescriptor) {
        this.packageDescriptor = packageDescriptor;
    }

    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entityClass) {
        return packageReader
                .readEntities(entityClass, packageDescriptor);
    }

    public Stream<CsvEntry> readRawCsvEntries(final String fileName) {
        return packageReader
                .readRawCsvEntries(fileName, packageDescriptor);
    }

    public <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entityClass) {
        if (this.packageDescriptor.getPackageScope().equals(PackageScope.FULL_PACKAGE)) {
            throw new UnsupportedOperationException("Only Delta packages contain timestamps");
        }
        return packageReader.readUuidsOfTimestampedEntities(entityClass, packageDescriptor);
    }

    public <ENTITYTYPE> boolean checkWhetherPackageContainsEntity(final ENTITYTYPE entity,
                                                                  final PackageDescriptor packageDescriptor) {
        return packageReader
                .checkWhetherPackageContainsEntity(entity, packageDescriptor);
    }

    public boolean checkWhetherPackageContainsEntity(final String fileName, final CsvEntry currentEntity) {

        return packageReader
                .checkWhetherPackageContainsEntity(fileName, currentEntity, packageDescriptor);
    }

    public Stream<PackedTableMetadata> getPackageMetadata() {
        return packageReader.getPackageMetadata(packageDescriptor);
    }

    public PackageDescriptor getPackageDescriptor() {
        return packageDescriptor;
    }

    public boolean doesContainFile(final String fileName) {
        return packageReader.doesContainFile(fileName, packageDescriptor);
    }
}
