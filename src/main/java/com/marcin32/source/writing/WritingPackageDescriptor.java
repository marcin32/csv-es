package com.marcin32.source.writing;

public class WritingPackageDescriptor  {

//    private final FileWriter fileWriter = new FileWriter();
//
//    private final FileDescriptors fileDescriptors = new FileDescriptors();
//
//    public WritingPackageDescriptor(final Long timestamp, final Path path, final PackageScope packageScope,
//                                    final PackageType packageType) {
//        super(timestamp, path, packageScope, packageType);
//    }

    /*public <ENTITYTYPE> void storeEntity(final String uuid, final String tableName, final ENTITYTYPE entity) {
        final CsvWriter csvWriter = new CsvWriter(this, fileDescriptors);
        final String fileName = tableName + Constants.TABLE_EXTENSION;
        csvWriter.saveEntity(uuid, fileName, entity);
    }*/

    /*public void storeRawEntity(final String fileName, final AbstractCsvEntity currentEntity) {
        final String content = currentEntity.toParsedContent();

        fileWriter.appendFile(content, fileName, this, fileDescriptors);
    }*/

    /*public void saveUpdateAndFinish() {
        final DeltaProcessingUtil deltaProcessingUtil = new DeltaProcessingUtil();
        deltaProcessingUtil.saveUpdateAndFinish(this);
    }*/

    /*void storeTimestampForEntity(final String uuid,
                                 final String tableName) {
        final String fileName = tableName + Constants.TABLE_EXTENSION;
        fileWriter.appendFile(uuid, TIMESTAMPS_PREFIX + fileName, this, fileDescriptors);
    }*/

//    void archiveCurrentUpdate() {
//
//        final String updateDirectory = this.getPath().getFileName().toString();
//        final File[] files = FilesystemDal.listFiles(this.getPath());
//
//
//        final String updateArchiveName = updateDirectory + Constants.ARCHIVE_EXTENSION;
//        final Path pathToOutputArchive = getPath().getParent().resolve(updateArchiveName);
//
//        try {
//            FilesystemDal.compress(pathToOutputArchive.toString(), files);
//            FilesystemDal.deleteFolder(getPath().toFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    Set<String> getAllStoredFileNames() {
//        return fileDescriptors.getFileNames();
//    }

    /*ReadingPackageDescriptor getReadingDescriptorForCurrentPackage() {
        return new ReadingPackageDescriptor(this.getTimestamp(), this.getPath(),
                this.getUpdateScope(), this.getUpdateType());
    }*/

//    void closeFiles() {
//        fileDescriptors.closeAllFiles();
//    }
}
