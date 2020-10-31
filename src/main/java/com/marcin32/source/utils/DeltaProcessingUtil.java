package com.marcin32.source.utils;

import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.dal.highlevel.database.PackageManager;
import com.marcin32.source.dal.highlevel.database.PackageReaderWrapper;
import com.marcin32.source.dal.highlevel.database.PackageWriterWrapper;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.model.PackedTableMetadata;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeltaProcessingUtil {

    private final PackageDescriptor currentFullPackage;

    public DeltaProcessingUtil(final PackageDescriptorForWriting packageWriterWrapper) {
        this.currentFullPackage = prepareDescriptor(packageWriterWrapper);
    }

    private PackageDescriptor prepareDescriptor(final PackageDescriptorForWriting packageWriterWrapper) {
        return new PackageDescriptor(packageWriterWrapper.getTimestamp(),
                packageWriterWrapper.getPackageScope(),
                PackageType.ARCHIVE,
                packageWriterWrapper.getBasePathToPackageLocation());
    }

    public void createDelta() {

        final Optional<PackageDescriptor> lastFullPackage = getPreviousPackageDescriptorIfExist();
        final PackageReaderWrapper currentPackagePackageReader = PackageManager.getPackageReader(currentFullPackage);

        if (lastFullPackage.isPresent()) {
            final PackageReaderWrapper lastFullPackagePackageReader = PackageManager.getPackageReader(lastFullPackage.get());
            prepareDeltaPackage(currentPackagePackageReader, lastFullPackagePackageReader);
        } else {
            copyCurrentUpdateAsDeltaUpdate(currentPackagePackageReader);
        }
    }

    private Optional<PackageDescriptor> getPreviousPackageDescriptorIfExist() {

        final Path parentDirectory = currentFullPackage.getBasePathToPackageLocation();

        final List<PackageDescriptor> previousFullUpdates = PackageManager.streamFullPackages(parentDirectory)
                .filter(packageDescriptor -> !packageDescriptor.getTimestamp().equals(currentFullPackage.getTimestamp()))
                .filter(packageDescriptor -> packageDescriptor.getTimestamp() < currentFullPackage.getTimestamp())
                .sorted(PackageManager::sort)
                .collect(Collectors.toList());

        if (!previousFullUpdates.isEmpty()) {
            Collections.reverse(previousFullUpdates);
            return previousFullUpdates
                    .stream()
                    .findFirst();
        }

        return Optional.empty();
    }

    private void copyCurrentUpdateAsDeltaUpdate(final PackageReaderWrapper currentFullPackage) {

        final PackageDescriptor currentPackageDescriptor = currentFullPackage.getPackageDescriptor();

        try {
            final Path source = currentPackageDescriptor.getBasePathWithPackageName();
            final PackageDescriptor targetPackageDescriptor = new PackageDescriptor(currentPackageDescriptor.getTimestamp(),
                    PackageScope.DELTA_PACKAGE, currentPackageDescriptor.getPackageType(), currentPackageDescriptor.getBasePathToPackageLocation());
            final Path target = targetPackageDescriptor.getBasePathWithPackageName();
            FilesystemDal.copyFile(source, target);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    void prepareDeltaPackage(final PackageReaderWrapper currentFullPackage,
                             final PackageReaderWrapper previousFullPackage) {

        try (final PackageWriterWrapper deltaWriter = PackageManager.createDeltaWriter(this.currentFullPackage);
             final Stream<PackedTableMetadata> fileNames = currentFullPackage.getPackageMetadata()) {
            fileNames
                    .filter(packedTableMetadata -> !packedTableMetadata.getFileName().equals(Constants.METADATA_FILENAME))
                    .forEach(fileName -> processSingleFile(fileName,
                            currentFullPackage, previousFullPackage, deltaWriter));

            deltaWriter.finalizedrdr();
            currentFullPackage.finalizedrdr();
            previousFullPackage.finalizedrdr();
        }
    }

    private void processSingleFile(final PackedTableMetadata tableMetadata,
                                   final PackageReaderWrapper currentFullPackage,
                                   final PackageReaderWrapper previousFullPackage,
                                   final PackageWriterWrapper deltaWriter) {
        // TODO: optimize method
        //if (previousFullPackage.doesContainFile(tableMetadata.getFileName())) {
        currentFullPackage.readRawCsvEntries(tableMetadata.getFileName())
                .forEach(entry -> processEntityForDelta(previousFullPackage,
                        deltaWriter, tableMetadata.getFileName(), entry));
        //} else {
        //    previousFullPackage.getPackageDescriptor().get
        //    FilesystemDal.copyFile();
        //}

        // TODO: performance improvement: optimize - clear cache after processing each file
    }

    private void processEntityForDelta(final PackageReaderWrapper previousFullUpdate,
                                       final PackageWriterWrapper deltaPackage,
                                       final String fileName,
                                       final CsvEntry currentEntity) {
        if (previousFullUpdate.checkWhetherPackageContainsEntity(fileName, currentEntity)) {
            final String timestampedFilename = fileName.replace(Constants.TABLE_EXTENSION, "")
                    + Constants.TIMESTAMPS_SUFFIX + Constants.TABLE_EXTENSION;
            deltaPackage.storeTimestampForEntity(currentEntity.getUuid(), timestampedFilename);
        } else {
            deltaPackage.storeRawEntity(fileName, currentEntity);
        }
    }
}
