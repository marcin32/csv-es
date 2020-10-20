package com.marcin32.source.dal.highlevel.manager;

import com.marcin32.source.dal.highlevel.database.PackageWriterWrapper;
import com.marcin32.source.model.PackageDescriptor;

import java.nio.file.Path;
import java.util.stream.Stream;

public class PackageManager {

    public PackageWriterWrapper createNewPackage(final Path basePath) {
        return null;
    }

    public PackageDescriptor getLatestDeltaPackage(final Path basePath) {
        return null;
    }

    public Stream<PackageDescriptor> streamAllPackages(final Path basePath) {
        return null;
    }

    public void movePackageToBackups(final PackageDescriptor packageDescriptor) {

    }


}
