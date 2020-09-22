package com.badscience.source.writing;

public class DeltaProcessingUtil {

    /*private final FilesystemDal filesystemDal = new FilesystemDal();

    public void saveUpdateAndFinish(final WritingPackageDescriptor currentFullUpdateWriter) {
        currentFullUpdateWriter.closeFiles();
        final Path parentDirectory = currentFullUpdateWriter.getPath().getParent();

        final ReadingDescriptorsDal readingDescriptorsDal = new ReadingDescriptorsDal();
        final Optional<ReadingPackageDescriptor> lastFullUpdate = readingDescriptorsDal
                .getLastArchivedFullUpdate(parentDirectory);

        if (lastFullUpdate.isPresent()) {
            final ReadingPackageDescriptor currentPackageReader = currentFullUpdateWriter
                    .getReadingDescriptorForCurrentPackage();
            prepareDeltaPackage(currentFullUpdateWriter, currentPackageReader, lastFullUpdate.get());
            currentFullUpdateWriter.archiveCurrentUpdate();
        } else {
            currentFullUpdateWriter.archiveCurrentUpdate();
            copyCurrentUpdateAsDeltaUpdate(currentFullUpdateWriter);
        }

        moveOldPackagesToBackupDirectory(parentDirectory);
    }

    private void copyCurrentUpdateAsDeltaUpdate(final WritingPackageDescriptor currentFullUpdateWriter) {

        try {
            final Path current = new File(currentFullUpdateWriter.getPath().toString() + Constants.ARCHIVE_EXTENSION).toPath();
            final Path target = currentFullUpdateWriter.getPath().getParent().resolve(currentFullUpdateWriter.getTimestamp() + "-" +
                    UpdateScope.DELTA.toString() + Constants.ARCHIVE_EXTENSION);
            filesystemDal.copyFile(current, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void prepareDeltaPackage(final WritingPackageDescriptor currentFullUpdateWriter,
                             final ReadingPackageDescriptor currentPackage,
                             final ReadingPackageDescriptor previousPackage) {

        final WritingDescriptorsDal writingDescriptorsDal = new WritingDescriptorsDal();
        final WritingPackageDescriptor deltaDescriptor = writingDescriptorsDal
                .getDescriptorForWritingDelta(currentFullUpdateWriter);

        final Set<String> fileNames = currentFullUpdateWriter.getAllStoredFileNames();

        for (final String fileName : fileNames) {
            try (final Stream<AbstractCsvEntity> currentFileEntityStream = currentPackage.readEntities(fileName)) {
                currentFileEntityStream
                        .forEach(entity -> processEntityForDelta(previousPackage, deltaDescriptor, fileName, entity));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processEntityForDelta(final ReadingPackageDescriptor previousFullUpdate,
                                       final WritingPackageDescriptor deltaPackage,
                                       final String fileName,
                                       final AbstractCsvEntity currentEntity) {
        try {
            if (!previousFullUpdate.isEntityAlreadyPersisted(fileName, currentEntity)) {
                deltaPackage.storeRawEntity(fileName, currentEntity);
            } else {
                deltaPackage.storeTimestampForEntity(currentEntity.getUuid(), fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveOldPackagesToBackupDirectory(final Path parent) {

        filesystemDal
                .createDirectoryIfNotExists(parent, BACKUP_DIRECTORY_NAME);

        final ReadingDescriptorsDal readingDescriptorsDal = new ReadingDescriptorsDal();
        final Optional<ReadingPackageDescriptor> lastDelta = readingDescriptorsDal
                .getLastDelta(parent);
        final Optional<ReadingPackageDescriptor> lastFullUpdate = readingDescriptorsDal
                .getLastFullUpdate(parent);

        if (lastDelta.isPresent() && lastFullUpdate.isPresent()) {
            final String lastDeltaPackageName = lastDelta.get().getPackageName();
            final String lastFullUpdatePackageName = lastFullUpdate.get().getPackageName();

            final File[] files = filesystemDal.listFiles(parent);

            for (final File file : files) {
                if (!file.isDirectory()) {
                    final String fileName = file.getName();
                    if (!(fileName.equals(lastDeltaPackageName) || fileName.equals(lastFullUpdatePackageName)) &&
                            fileName.contains(UpdateScope.FULL_UPDATE.toString())) {
                        filesystemDal.moveIntoSubdirectory(file, BACKUP_DIRECTORY_NAME);
                    }
                }
            }
        }
    }*/
}
