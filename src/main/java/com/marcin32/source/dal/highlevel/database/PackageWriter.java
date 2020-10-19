package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.Constants;
import com.marcin32.source.dal.highlevel.table.TableWriter;
import com.marcin32.source.dal.lowlevel.csv.CsvWriter;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.MetadataAdapter;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;

import java.util.Map;
import java.util.stream.Stream;

class PackageWriter implements IPackageDal {

    public static final MetadataAdapter METADATA_FORMAT_ADAPTER = new MetadataAdapter();
    private final PackageReader packageReader = new PackageReader();

    private final TableWriter tableWriter = new TableWriter();

    public <ENTITYTYPE> void storeEntity(final String entityId,
                                         final ENTITYTYPE entity,
                                         final PackageDescriptorForWriting packageDescriptorForWriting) {
        packageDescriptorForWriting
                .logEntity(entity);
        final RawFile tableFile = packageDescriptorForWriting
                .getFileForWritingEntities(entity);
        tableWriter
                .writeEntity(tableFile, entityId, entity);
    }

    void closePackage(final PackageDescriptorForWriting packageDescriptor) {
        FilesystemDal.archivePackage(packageDescriptor);
    }

    void saveMetadata(final PackageDescriptorForWriting packageDescriptor) {
        final CsvWriter csvWriter = new CsvWriter();
        final RawFile metadataFile = (RawFile) packageDescriptor.getMetadataFile();
        packageDescriptor
                .getListOfMetadataForWriting()
                .forEach(meta -> formatAndStoreMetadata(meta, csvWriter, metadataFile));
        csvWriter.closeCsvFile(metadataFile);

    }

    private void formatAndStoreMetadata(final Map.Entry<String, Long> meta,
                                        final CsvWriter csvWriter,
                                        final RawFile metadataFile) {
        final String tableName = meta.getKey() + Constants.TABLE_EXTENSION;
        csvWriter
                .saveEntity(metadataFile, METADATA_FORMAT_ADAPTER, tableName,
                        meta.getValue().toString());
    }

    @Override
    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entitytype,
                                                                     final PackageDescriptor packageDescriptor) {
        return packageReader.readEntities(entitytype, packageDescriptor);
    }

    @Override
    public <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entitytype,
                                                                      final PackageDescriptor packageDescriptor) {
        return packageReader.readUuidsOfTimestampedEntities(entitytype, packageDescriptor);
    }

    @Override
    public <ENTITYTYPE> boolean checkWhetherPackageContainsEntity(final ENTITYTYPE entity,
                                                                  final PackageDescriptor packageDescriptor) {
        return packageReader.checkWhetherPackageContainsEntity(entity, packageDescriptor);
    }

    @Override
    public <ENTITYTYPE> long numberOfEntities(final Class<ENTITYTYPE> entity,
                                              final PackageDescriptor packageDescriptor) {
        return packageReader.numberOfEntities(entity, packageDescriptor);
    }

    @Override
    public Stream<PackedTableMetadata> getPackageMetadata(final PackageDescriptor packageDescriptor) {
        return packageReader.getPackageMetadata(packageDescriptor);
    }
}
