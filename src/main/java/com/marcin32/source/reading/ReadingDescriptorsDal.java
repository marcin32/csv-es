package com.marcin32.source.reading;

public final class ReadingDescriptorsDal {

    //private final FilesystemDal filesystemDal = new FilesystemDal();

   /* public Stream<ReadingPackageDescriptor> getDescriptorsForReading(final Path baseDirectoryPath,
                                                                     final UpdateScope updateScope) {
        return filesystemDal.listUpdates(baseDirectoryPath)
                .stream()
                .sorted((info1, info2) -> info2.getTimestamp().compareTo(info1.getTimestamp()))
                .filter(descriptor -> descriptor.getUpdateScope().equals(updateScope));
    }

    Optional<ReadingPackageDescriptor> getLastDescriptorForReading(final Path baseDirectoryPath,
                                                                   final UpdateScope updateScope) {
        return getDescriptorsForReading(baseDirectoryPath, updateScope)
                .findFirst();
    }

    Optional<ReadingPackageDescriptor> getLastArchivedDescriptorForReading(final Path baseDirectoryPath,
                                                                           final UpdateScope updateScope) {
        return getDescriptorsForReading(baseDirectoryPath, updateScope)
                .filter(descriptor -> descriptor.getUpdateType().equals(UpdateType.ARCHIVE))
                .findFirst(); // TODO: write test, to check whether the freshest one is selected
    }

    public Optional<ReadingPackageDescriptor> getLastDelta(final Path baseDirectoryPath) {
        return getLastDescriptorForReading(baseDirectoryPath, UpdateScope.DELTA);
    }

    public Optional<ReadingPackageDescriptor> getLastFullUpdate(final Path baseDirectoryPath) {
        return getLastDescriptorForReading(baseDirectoryPath, UpdateScope.FULL_UPDATE);
    }

    public Optional<ReadingPackageDescriptor> getLastArchivedFullUpdate(final Path baseDirectoryPath) {
        return getLastArchivedDescriptorForReading(baseDirectoryPath, UpdateScope.FULL_UPDATE);
    }*/
}
