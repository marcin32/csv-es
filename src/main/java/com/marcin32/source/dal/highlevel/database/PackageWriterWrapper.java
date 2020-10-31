package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.PackageScope;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.utils.DeltaProcessingUtil;
import com.marcin32.source.utils.FilesystemDal;

import java.nio.file.Path;
import java.util.Map;

public class PackageWriterWrapper implements AutoCloseable {

    private final PackageWriter packageWriter = new PackageWriter();

    private final PackageDescriptorForWriting packageDescriptorForWriting;

    PackageWriterWrapper(final PackageDescriptorForWriting packageDescriptorForWriting) {
        this.packageDescriptorForWriting = packageDescriptorForWriting;
    }

    public <ENTITYTYPE> void storeEntity(final String entityId,
                                         final ENTITYTYPE entity) {
        packageWriter
                .storeEntity(entityId, entity, packageDescriptorForWriting);
    }

    public void storeRawEntity(final String fileName, final CsvEntry entry) {
        packageWriter
                .storeRawEntity(fileName, entry, packageDescriptorForWriting);
    }

    public void storeTimestampForEntity(final String entityId, final String fileName) {
        packageWriter
                .storeEntityTimestamp(entityId, fileName, packageDescriptorForWriting);
    }

    @Override
    public void close() {
        if (containsAnyRecords(packageDescriptorForWriting)) {
            packageWriter.saveMetadata(packageDescriptorForWriting);
            packageWriter.closePackage(packageDescriptorForWriting);

            if (this.packageDescriptorForWriting.getPackageScope().equals(PackageScope.FULL_PACKAGE)) {
                final DeltaProcessingUtil deltaProcessingUtil = new DeltaProcessingUtil(packageDescriptorForWriting);
                deltaProcessingUtil.createDelta();
            }
        } else {
            final Path basePathWithPackageName = packageDescriptorForWriting.getBasePathWithPackageName();
            FilesystemDal.deleteFolder(basePathWithPackageName.toFile());
        }
    }

    private boolean containsAnyRecords(final PackageDescriptorForWriting packageDescriptorForWriting) {
        return packageDescriptorForWriting
                .getListOfMetadataForWriting()
                .stream()
                .mapToLong(Map.Entry::getValue)
                .sum() > 0;
    }

    public void finalizedrdr() {
        //packageWriter.finalizedrdr();
    }
}
