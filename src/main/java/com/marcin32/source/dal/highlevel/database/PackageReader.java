package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.dal.highlevel.table.CachedTableReader;
import com.marcin32.source.model.ITableMetadata;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.MetadataAdapter;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;

import java.util.stream.Stream;

class PackageReader implements IPackageDal {

    private final CachedTableReader tableReader = new CachedTableReader();

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
    public <ENTITYTYPE> long numberOfEntities(final Class<ENTITYTYPE> entity,
                                              final PackageDescriptor packageDescriptor) {
        return getTableEntityFile(entity, packageDescriptor).getNumberOfEntries();
    }

    @Override
    public Stream<PackedTableMetadata> getPackageMetadata(final PackageDescriptor packageDescriptor) {
        final AbstractFile metadataFile = packageDescriptor.getMetadataFile();
        return tableReader.readEntities(metadataFile, METADATA_FORMAT_ADAPTER);
    }

    <ENTITYTYPE> AbstractFile getTableTimestampFile(final Class<ENTITYTYPE> entityType, final PackageDescriptor packageDescriptor) {

        final String tableFileName = entityType.getSimpleName() + Constants.TIMESTAMPS_SUFFIX + Constants.TABLE_EXTENSION;
        return getTableFile(packageDescriptor, tableFileName);
    }

    <ENTITYTYPE> AbstractFile getTableEntityFile(final Class<ENTITYTYPE> entityType, final PackageDescriptor packageDescriptor) {

        final String tableFileName = entityType.getSimpleName() + Constants.TABLE_EXTENSION;
        return getTableFile(packageDescriptor, tableFileName);
    }

    private AbstractFile getTableFile(final PackageDescriptor packageDescriptor,
                                      final String anObject) {
        return getPackageMetadata(packageDescriptor)
                .filter(tableMetadata -> tableMetadata.getClassName().equals(anObject))
                .findFirst()
                .map(tableMetadata -> mapTableMetadataFile(tableMetadata, packageDescriptor))
                .orElseThrow();
    }

    private AbstractFile mapTableMetadataFile(final ITableMetadata tableMetadataFile,
                                              final PackageDescriptor packageDescriptor) {
        if (packageDescriptor.getPackageType().equals(PackageType.ARCHIVE)) {
            return new PackedFile(tableMetadataFile.getClassName(),
                    tableMetadataFile.getNumberOfEntities(),
                    packageDescriptor.getBasePathWithPackageName());
        }
        return new RawFile(tableMetadataFile.getClassName(),
                tableMetadataFile.getNumberOfEntities(),
                packageDescriptor.getBasePathWithPackageName());
    }
}
