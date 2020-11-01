package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.PackageScope;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;

import java.util.*;
import java.util.stream.Stream;

public class PackageReaderWrapper {

    private final PackageReader packageReader = new PackageReader();

    private final PackageDescriptor packageDescriptor;

    PackageReaderWrapper(final PackageDescriptor packageDescriptor) {
        this.packageDescriptor = packageDescriptor;
    }

    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final Class<ENTITYTYPE> entityClass) {
        return packageReader
                .readEntities(entityClass, packageDescriptor);
    }

    public Stream<CsvEntry> readRawCsvEntries(final String fileName) {
        return packageReader
                .readRawCsvEntries(fileName, packageDescriptor);
    }

    public <ENTITYTYPE> Stream<String> readUuidsOfTimestampedEntities(final Class<ENTITYTYPE> entityClass) {
        if (this.packageDescriptor.getPackageScope().equals(PackageScope.FULL_PACKAGE)) {
            throw new UnsupportedOperationException("Only Delta packages contain timestamps");
        }
        return packageReader.readUuidsOfTimestampedEntities(entityClass, packageDescriptor);
    }

    public boolean checkWhetherPackageMightContainEntity(final String fileName, final CsvEntry currentEntity) {

        return packageReader
                .checkWhetherPackageContainsEntity(fileName, currentEntity, packageDescriptor);
    }

    public Stream<PackedTableMetadata> getPackageMetadata() {
        return packageReader.getPackageMetadata(packageDescriptor);
    }

    public PackageDescriptor getPackageDescriptor() {
        return packageDescriptor;
    }

    public boolean doesContainFile(final String fileName) {
        return packageReader.doesContainFile(fileName, packageDescriptor);
    }

    public void finalizedrdr() {
        packageReader.finalizedrdr();
    }

    public Map<Boolean, Set<CsvEntry>> splitEntitiesIntoContainedOrNot(final String fileName,
                                                                       final List<CsvEntry> entitiesFromCurrentPackage) {
        final Map<Boolean, Set<CsvEntry>> containedToEntries = new HashMap<>();
        //final List<String> hashesFromCurrentPackage = entitiesFromCurrentPackage
        //        .stream()
        //        .map(CsvEntry::getShaContentHash)
        //        .collect(Collectors.toList());


        try (final Stream<CsvEntry> csvEntryStream = readRawCsvEntries(fileName)) {
            csvEntryStream
                    .forEach(entryFromPreviousPackage -> divide(entryFromPreviousPackage, containedToEntries, entitiesFromCurrentPackage));
        }

        return containedToEntries;
    }

    private void divide(final CsvEntry entryFromPreviousPackage,
                        final Map<Boolean, Set<CsvEntry>> containedToEntries,
                        final List<CsvEntry> entitiesFromCurrentPackage) {
        final String uuid = entryFromPreviousPackage.getUuid();
        if (entitiesFromCurrentPackage.stream().anyMatch(e -> e.getUuid().equals(uuid))) {
            if (entitiesFromCurrentPackage.stream()
                    .anyMatch(e ->e.getShaContentHash()
                            .equals((entryFromPreviousPackage.getShaContentHash())))) {
                addToContainer(containedToEntries, entryFromPreviousPackage, true);
            } else {
                addToContainer(containedToEntries, entryFromPreviousPackage, false);
            }
        }
    }

    private void addToContainer(final Map<Boolean, Set<CsvEntry>> containedToEntries,
                                final CsvEntry entry,
                                final boolean isContained) {
        if (!containedToEntries.containsKey(isContained)) {
            containedToEntries.put(isContained, new HashSet<>());
        }
        containedToEntries.get(isContained).add(entry);
    }
}
