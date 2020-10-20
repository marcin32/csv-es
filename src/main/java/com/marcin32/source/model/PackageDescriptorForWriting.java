package com.marcin32.source.model;

import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.file.RawFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PackageDescriptorForWriting extends PackageDescriptor {

    private final Map<String, Long> fileNameToLineCount = new HashMap<>();
    private final Map<String, RawFile> classNameToRawFile = new HashMap<>();

    public PackageDescriptorForWriting(final Long timestamp,
                                       final PackageScope packageScope,
                                       final Path basePathToPackageLocation) {
        super(timestamp, packageScope, PackageType.DIRECTORY, basePathToPackageLocation);

        final File basePathWithPackageName = this.getBasePathWithPackageName().toFile();
        basePathWithPackageName.mkdirs();
    }

    public <ENTITYTYPE> void logEntity(final ENTITYTYPE entity) {
        final String fileName = getEntitiesFileName(entity);
        logLineForFileName(fileName);
    }

    public <ENTITYTYPE> void logTimestampForEntity(final ENTITYTYPE entity) {
        final String fileName = getTimestampedFileName(entity);
        logLineForFileName(fileName);
    }

    public <ENTITYTYPE> RawFile getFileForWritingTimestamps(final ENTITYTYPE entity) {
        final String fileName = getTimestampedFileName(entity);
        return prepareRawFile(fileName);
    }

    public <ENTITYTYPE> RawFile getFileForWritingEntities(final ENTITYTYPE entity) {
        final String fileName = getEntitiesFileName(entity);
        return prepareRawFile(fileName);
    }

    private <ENTITYTYPE> String getEntitiesFileName(ENTITYTYPE entity) {
        return entity.getClass().getSimpleName() + Constants.TABLE_EXTENSION;
    }

    private <ENTITYTYPE> String getTimestampedFileName(final ENTITYTYPE entity) {
        return entity.getClass().getSimpleName() + Constants.TIMESTAMPS_SUFFIX + Constants.TABLE_EXTENSION;
    }

    private void logLineForFileName(String fileName) {
        if (fileNameToLineCount.containsKey(fileName)) {
            final Long newValue = fileNameToLineCount.get(fileName) + 1;
            fileNameToLineCount
                    .put(fileName, newValue);
        } else {
            fileNameToLineCount
                    .put(fileName, 1L);
        }
    }

    private RawFile prepareRawFile(String fileName) {
        if (!classNameToRawFile.containsKey(fileName)) {
            synchronized (this) {
                if (!classNameToRawFile.containsKey(fileName)) {
                    final RawFile rawFile = new RawFile(fileName, this.getBasePathWithPackageName());
                    createIfNotExists(rawFile);
                    classNameToRawFile.put(fileName, rawFile);
                    return rawFile;
                }

            }
        }
        return classNameToRawFile.get(fileName);
    }

    private void createIfNotExists(final RawFile rawFile) {
        final File file = rawFile.getDirectoryPathWithFileName().toFile();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<Map.Entry<String, Long>> getListOfMetadataForWriting() {
        return fileNameToLineCount.entrySet();
    }
}
