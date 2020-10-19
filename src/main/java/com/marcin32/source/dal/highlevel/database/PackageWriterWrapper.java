package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.model.PackageDescriptorForWriting;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PackageWriterWrapper implements AutoCloseable {

    private final PackageWriter packageWriter;

    private final PackageDescriptorForWriting packageDescriptorForWriting;

    public void writeEntity() {

    }

    public void closePackage() {

    }

    @Override
    public void close() throws Exception {
        packageWriter.saveMetadata(packageDescriptorForWriting);
        packageWriter.closePackage(packageDescriptorForWriting);
    }
}
