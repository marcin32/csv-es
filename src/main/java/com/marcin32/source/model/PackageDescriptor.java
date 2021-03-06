package com.marcin32.source.model;

import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;

import java.io.IOException;
import java.nio.file.Path;

import static com.marcin32.source.base.Constants.ARCHIVE_EXTENSION;

public class PackageDescriptor {

    Long timestamp;

    PackageScope packageScope;

    PackageType packageType;

    Path basePathToPackageLocation;

    public PackageDescriptor(Long timestamp, PackageScope packageScope, PackageType packageType, Path basePathToPackageLocation) {
        this.timestamp = timestamp;
        this.packageScope = packageScope;
        this.packageType = packageType;
        this.basePathToPackageLocation = basePathToPackageLocation;
    }

    public String getPackageName() {
        final StringBuilder sb = new StringBuilder(String.valueOf(timestamp));
        sb.append("-").append(packageScope.toString());
        if(packageType.equals(PackageType.ARCHIVE)) {
            sb.append(ARCHIVE_EXTENSION);
        }

        return sb.toString();
    }

    public Path getBasePathWithPackageName() {
        return basePathToPackageLocation.resolve(getPackageName());
    }

    public AbstractFile getMetadataFile() {
        if (this.packageType.equals(PackageType.ARCHIVE)) {
            int tarEntries = 0;
            try {
                // TODO: test method
                tarEntries = FilesystemDal.countTarEntries(getBasePathWithPackageName().toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new PackedFile(Constants.METADATA_FILENAME, tarEntries, getBasePathWithPackageName());
        }
        return new RawFile(Constants.METADATA_FILENAME, getBasePathWithPackageName());
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public PackageScope getPackageScope() {
        return this.packageScope;
    }

    public PackageType getPackageType() {
        return this.packageType;
    }

    public Path getBasePathToPackageLocation() {
        return this.basePathToPackageLocation;
    }
}
