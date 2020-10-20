package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackageDescriptorForWriting;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackageWriterWrapper implements AutoCloseable {

    private final PackageWriter packageWriter;

    private final PackageDescriptorForWriting packageDescriptorForWriting;

    public <ENTITYTYPE> void writeEntity(final String entityId,
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
