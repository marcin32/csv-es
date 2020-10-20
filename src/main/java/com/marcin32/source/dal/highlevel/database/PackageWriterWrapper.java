package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackageDescriptorForWriting;

public class PackageWriterWrapper implements AutoCloseable {

    private final PackageWriter packageWriter = new PackageWriter();

    private final PackageDescriptorForWriting packageDescriptorForWriting;

    public PackageWriterWrapper(final PackageDescriptorForWriting packageDescriptorForWriting) {
        this.packageDescriptorForWriting = packageDescriptorForWriting;
    }

    public <ENTITYTYPE> void storeEntity(final String entityId,
                                         final ENTITYTYPE entity) {
        packageWriter
                .storeEntity(entityId, entity, packageDescriptorForWriting);
    }

    @Override
    public void close() throws Exception {
        packageWriter.saveMetadata(packageDescriptorForWriting);
        packageWriter.closePackage(packageDescriptorForWriting);
    }
}
