package com.marcin32.source.utils;

import static java.util.Spliterator.ORDERED;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

            // TODO: clean up this after debugging
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

        final Stream<CsvEntry> preFilteredEntriesFromCurrentPackage = currentFullPackage
                .readRawCsvEntries(tableMetadata.getFileName())
                .map(entry -> preFilterEntityCandidates(previousFullPackage,
                        deltaWriter, tableMetadata.getFileName(), entry))
                .filter(Optional::isPresent)
                .map(Optional::get);

        BatchingIterator.batchedStreamOf(preFilteredEntriesFromCurrentPackage, 100)
                .forEach(entriesFromCurrentPackage -> processEntityForDelta(previousFullPackage,
                        deltaWriter, tableMetadata.getFileName(), entriesFromCurrentPackage));
        //.forEach(System.out::println);


        //final List<CsvEntry> collect = currentFullPackage.readRawCsvEntries(tableMetadata.getFileName())


        //} else {
        //    previousFullPackage.getPackageDescriptor().get
        //    FilesystemDal.copyFile();
        //}

        // TODO: performance improvement: optimize - clear cache after processing each file
    }


    private Optional<CsvEntry> preFilterEntityCandidates(final PackageReaderWrapper previousFullUpdate,
                                                         final PackageWriterWrapper deltaPackage,
                                                         final String fileName,
                                                         final CsvEntry currentEntity) {
        if (previousFullUpdate.checkWhetherPackageMightContainEntity(fileName, currentEntity)) {
            return Optional.of(currentEntity);
        } else {
            deltaPackage.storeRawEntity(fileName, currentEntity);
            return Optional.empty();
        }
    }

    private void processEntityForDelta(final PackageReaderWrapper previousFullUpdate,
                                       final PackageWriterWrapper deltaPackage,
                                       final String fileName,
                                       final List<CsvEntry> entriesFromCurrentPackage) {
        final Map<Boolean, Set<CsvEntry>> booleanSetMap = previousFullUpdate
                .splitEntitiesIntoContainedOrNot(fileName, entriesFromCurrentPackage);

        if (booleanSetMap.containsKey(true)) {
            for (final CsvEntry entryToTimestamp : booleanSetMap.get(true)) {
                final String timestampedFilename = fileName.replace(Constants.TABLE_EXTENSION, "")
                        + Constants.TIMESTAMPS_SUFFIX + Constants.TABLE_EXTENSION;
                deltaPackage.storeTimestampForEntity(entryToTimestamp.getUuid(), timestampedFilename);
            }
        }
        if (booleanSetMap.containsKey(false)) {
            for (final CsvEntry entryToStore : booleanSetMap.get(false)) {

                deltaPackage.storeRawEntity(fileName, entryToStore);
            }
        }
    }

    /**
     * An iterator which returns batches of items taken from another iterator
     */
    public static class BatchingIterator<T> implements Iterator<List<T>> {
        /**
         * Given a stream, convert it to a stream of batches no greater than the
         * batchSize.
         *
         * @param originalStream to convert
         * @param batchSize      maximum size of a batch
         * @param <T>            type of items in the stream
         * @return a stream of batches taken sequentially from the original stream
         */
        public static <T> Stream<List<T>> batchedStreamOf(Stream<T> originalStream, int batchSize) {
            return asStream(new BatchingIterator<>(originalStream.iterator(), batchSize));
        }

        private static <T> Stream<T> asStream(Iterator<T> iterator) {
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(iterator, ORDERED),
                    false);
        }

        private final int batchSize;
        private List<T> currentBatch;
        private final Iterator<T> sourceIterator;

        public BatchingIterator(Iterator<T> sourceIterator, int batchSize) {
            this.batchSize = batchSize;
            this.sourceIterator = sourceIterator;
        }

        @Override
        public boolean hasNext() {
            prepareNextBatch();
            return currentBatch != null && !currentBatch.isEmpty();
        }

        @Override
        public List<T> next() {
            return currentBatch;
        }

        private void prepareNextBatch() {
            currentBatch = new ArrayList<>(batchSize);
            while (sourceIterator.hasNext() && currentBatch.size() < batchSize) {
                currentBatch.add(sourceIterator.next());
            }
        }
    }
}
