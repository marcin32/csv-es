package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.dal.highlevel.table.CachedTableReader;
import com.marcin32.source.model.*;
import com.marcin32.source.model.csv.MetadataAdapter;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class PackageReader implements IPackageDal {

    private final CachedTableReader tableReader = new CachedTableReader();

    private final Map<Pair<PackageDescriptor, String>, AbstractFile> openedFilesCache = new HashMap<>();

    public static final MetadataAdapter METADATA_FORMAT_ADAPTER = new MetadataAdapter();

    @Override
    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entityType,
                                                                     final PackageDescriptor packageDescriptor) {
        final AbstractFile tableFile = getTableEntityFile(entityType, packageDescriptor);
        return tableReader.readEntities(tableFile, entityType);
    }

    @Override
    public <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entityType,
                                                                      final PackageDescriptor packageDescriptor) {
        final AbstractFile tableFile = getTableTimestampFile(entityType, packageDescriptor);
        return tableReader.readUuidsOfTimestampedEntities(tableFile);
    }

    @Override
    public <ENTITYTYPE> boolean checkWhetherPackageContainsEntity(final ENTITYTYPE entity,
                                                                  final PackageDescriptor packageDescriptor) {
        final Class<?> entityClass = entity.getClass();
        final AbstractFile tableFile = getTableEntityFile(entityClass, packageDescriptor);
        return tableReader
                .checkWhetherTableContainsEntity(entity, tableFile);
    }

    @Override
    public boolean checkWhetherPackageContainsEntity(final String fileName,
                                                     final CsvEntry currentEntity,
                                                     final PackageDescriptor packageDescriptor) {
        final AbstractFile tableFile = getTableFile(fileName, packageDescriptor);
        return tableReader
                .checkWhetherTableContainsHash(currentEntity.getShaContentHash(), tableFile);
    }

    @Override
    public <ENTITYTYPE> long numberOfEntities(final Class<ENTITYTYPE> entity,
                                              final PackageDescriptor packageDescriptor) {
        return getTableEntityFile(entity, packageDescriptor).getNumberOfEntries();
    }

    @Override
    public Stream<PackedTableMetadata> getPackageMetadata(final PackageDescriptor packageDescriptor) {
        final AbstractFile metadataFile = packageDescriptor.getMetadataFile();
        return tableReader.readEntities(metadataFile, METADATA_FORMAT_ADAPTER);
    }

    @Override
    public boolean doesContainFile(final String fileName, final PackageDescriptor packageDescriptor) {
        return getPackageMetadata(packageDescriptor)
                .anyMatch(entry -> entry.equals(fileName));

    }

    @Override
    public Stream<CsvEntry> readRawCsvEntries(final String fileName, final PackageDescriptor packageDescriptor) {
        final AbstractFile file = mapTableMetadataFile(new PackedTableMetadata(fileName, 0), packageDescriptor);
        return tableReader
                .readRawCsvEntries(file);
    }

    <ENTITYTYPE> AbstractFile getTableTimestampFile(final Class<ENTITYTYPE> entityType,
                                                    final PackageDescriptor packageDescriptor) {

        final String tableFileName = entityType.getSimpleName() + Constants.TIMESTAMPS_SUFFIX + Constants.TABLE_EXTENSION;
        return getTableFile(tableFileName, packageDescriptor);
    }

    <ENTITYTYPE> AbstractFile getTableEntityFile(final Class<ENTITYTYPE> entityType,
                                                 final PackageDescriptor packageDescriptor) {

        final String tableFileName = entityType.getSimpleName() + Constants.TABLE_EXTENSION;
        return getTableFile(tableFileName, packageDescriptor);
    }

    private AbstractFile getTableFile(final String fileName,
                                      final PackageDescriptor packageDescriptor) {
        final Pair<PackageDescriptor, String> packageFileKey = new Pair<>(packageDescriptor, fileName);
        if(openedFilesCache.containsKey(packageFileKey)) {
            return openedFilesCache.get(packageFileKey);
        }

        final AbstractFile abstractFile = getPackageMetadata(packageDescriptor)
                .filter(tableMetadata -> tableMetadata.getFileName().equals(fileName))
                .findFirst()
                .map(tableMetadata -> mapTableMetadataFile(tableMetadata, packageDescriptor))
                .orElseThrow();
        openedFilesCache.put(packageFileKey, abstractFile);
        return abstractFile;
    }

    private AbstractFile mapTableMetadataFile(final ITableMetadata tableMetadataFile,
                                              final PackageDescriptor packageDescriptor) {
        if (packageDescriptor.getPackageType().equals(PackageType.ARCHIVE)) {
            return new PackedFile(tableMetadataFile.getFileName(),
                    tableMetadataFile.getNumberOfEntities(),
                    packageDescriptor.getBasePathWithPackageName());
        }
        return new RawFile(tableMetadataFile.getFileName(),
                tableMetadataFile.getNumberOfEntities(),
                packageDescriptor.getBasePathWithPackageName());
    }

    @Value
    private static class Pair<T1, T2> {
        T1 t1;
        T2 t2;
    }
}
