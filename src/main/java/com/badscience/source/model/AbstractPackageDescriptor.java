package com.badscience.source.model;

import com.badscience.source.base.PackageScope;
import com.badscience.source.base.PackageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

import static com.badscience.source.base.Constants.ARCHIVE_EXTENSION;

@Getter
@AllArgsConstructor
public abstract class AbstractPackageDescriptor {

    Long timestamp;

    Path path;

    PackageScope packageScope;

    PackageType packageType;

    public String getPackageName() {
        final StringBuilder sb = new StringBuilder(String.valueOf(timestamp));
        sb.append("-").append(packageScope.toString());
        if(packageType.equals(PackageType.ARCHIVE)) {
            sb.append(ARCHIVE_EXTENSION);
        }

        return sb.toString();
    }
}
