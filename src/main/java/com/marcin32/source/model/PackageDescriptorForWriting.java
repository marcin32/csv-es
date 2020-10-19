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

    private final Map<String, Long> classNameToEntityCount = new HashMap<>();
    private final Map<String, RawFile> classNameToRawFile = new HashMap<>();

    public PackageDescriptorForWriting(final Long timestamp,
                                       final PackageScope packageScope,
                                       final Path basePathToPackageLocation) {
        super(timestamp, packageScope, PackageType.DIRECTORY, basePathToPackageLocation);

        final File basePathWithPackageName = this.getBasePathWithPackageName().toFile();
        basePathWithPackageName.mkdirs();
    }

    public <ENTITYTYPE> void logEntity(final ENTITYTYPE entity) {
        final String className = entity.getClass().getSimpleName();
        if (classNameToEntityCount.containsKey(className)) {
            final Long newValue = classNameToEntityCount.get(className) + 1;
            classNameToEntityCount
                    .put(className, newValue);
        } else {
            classNameToEntityCount
                    .put(className, 1L);
        }
    }

    public <ENTITYTYPE> RawFile getFileForWritingEntities(final ENTITYTYPE entity) {
        final String fileName = entity.getClass().getSimpleName() + Constants.TABLE_EXTENSION;
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
        return classNameToEntityCount.entrySet();
    }
}
