package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.dal.highlevel.table.TableWriter;
import com.marcin32.source.dal.lowlevel.csv.CsvWriter;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.model.csv.MetadataAdapter;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;

import java.util.Map;

class PackageWriter {

    public static final MetadataAdapter METADATA_FORMAT_ADAPTER = new MetadataAdapter();
    //private final PackageReader packageReader = new PackageReader();

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

    public <ENTITYTYPE> void storeEntityTimestamp(final String entityId,
                                                            final ENTITYTYPE entity,
                                                            final PackageDescriptorForWriting packageDescriptorForWriting) {
        packageDescriptorForWriting
                .logTimestampForEntity(entity);
        final RawFile tableFile = packageDescriptorForWriting
                .getFileForWritingTimestamps(entity);
        tableWriter
                .writeTimestampForEntity(tableFile, entityId);
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
        final String tableName = meta.getKey();
        csvWriter
                .saveEntity(metadataFile, METADATA_FORMAT_ADAPTER, tableName,
                        meta.getValue().toString());
    }
}
