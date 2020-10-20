package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.utils.FilesystemDal;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.marcin32.source.base.Constants.BACKUP_DIRECTORY_NAME;

public final class PackageManager {

    public static PackageWriterWrapper createNewPackage(final Path basePath) {
        final long timestamp = System.currentTimeMillis();
        final PackageDescriptorForWriting packageDescriptorForWriting =
                new PackageDescriptorForWriting(timestamp, PackageScope.FULL_PACKAGE, basePath);
        return new PackageWriterWrapper(packageDescriptorForWriting);
    }

    public static Optional<PackageReaderWrapper> getLatestDeltaPackage(final Path basePath) {
        return streamSortedDeltaPackages(basePath)
                .reduce((a, b) -> b)
                .map(PackageReaderWrapper::new);
    }

    public static Optional<PackageReaderWrapper> getLatestFullPackage(final Path basePath) {
        return streamSortedFullPackages(basePath)
                .reduce((a, b) -> b)
                .map(PackageReaderWrapper::new);
    }

    public static Stream<PackageDescriptor> streamSortedFullPackages(final Path basePath) {
        return streamFullPackages(basePath)
                .sorted(PackageManager::sort);
    }

    public static Stream<PackageDescriptor> streamFullPackages(final Path basePath) {
        return streamAllPackages(basePath)
                .filter(packageDescriptor -> packageDescriptor.getPackageScope().equals(PackageScope.FULL_PACKAGE));
    }

    public static int sort(final PackageDescriptor package1,
                           final PackageDescriptor package2) {
        return (int) (package1.getTimestamp() - package2.getTimestamp());
    }

    public static Stream<PackageDescriptor> streamAllPackages(final Path basePath) {
        final Path backupdirectoryPath = basePath.resolve(BACKUP_DIRECTORY_NAME);
        final Stream<PackageDescriptor> descriptorsFromBackupDirectory;
        if (backupdirectoryPath.toFile().exists()) {
            descriptorsFromBackupDirectory =
                    streamPackagesFrom(backupdirectoryPath);
        } else {
            descriptorsFromBackupDirectory = Stream.empty();
        }
        final Stream<PackageDescriptor> descriptorsFromRootDirectory = streamPackagesFrom(basePath);

        return Stream.concat(descriptorsFromBackupDirectory, descriptorsFromRootDirectory);
    }

    public static Stream<PackageDescriptor> streamDeltaPackages(final Path basePath) {
        return streamAllPackages(basePath)
                .filter(packageDescriptor -> packageDescriptor.getPackageScope().equals(PackageScope.DELTA_PACKAGE));
    }

    public static Stream<PackageDescriptor> streamPackagesFrom(final Path resolve) {

        final File[] files = FilesystemDal.listFiles(resolve);

        return Arrays.stream(files)
                .map(PackageManager::tryToMatchDescriptor)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private static Optional<PackageDescriptor> tryToMatchDescriptor(final File file) {

        final String name = file.getName();

        if (name.contains("-") && name.contains(Constants.ARCHIVE_EXTENSION)) {
            final String[] split = name
                    .replace(Constants.ARCHIVE_EXTENSION, "")
                    .split("-");

            if (split.length == 2) {
                final Optional<PackageScope> packageScope = getPackageScope(split[1]);
                final Optional<Long> packageTimestamp = getPackageTimestamp(split[0]);

                if (packageScope.isPresent() && packageTimestamp.isPresent()) {
                    return Optional.of(new PackageDescriptor(packageTimestamp
                            .get(), packageScope.get(), PackageType.ARCHIVE, Path.of(file.getParent())));
                }
            }
        }

        return Optional.empty();
    }

    private static Optional<PackageScope> getPackageScope(final String scope) {
        return Arrays.stream(PackageScope.values())
                .filter(val -> val.name().equals(scope))
                .findFirst();
    }

    private static Optional<Long> getPackageTimestamp(final String timestamp) {
        try {
            final Long parsedTimestamp = Long.valueOf(timestamp);
            return Optional.of(parsedTimestamp);
        } catch (final Throwable th) {
            return Optional.empty();
        }
    }

    public static Stream<PackageDescriptor> streamAllDeltaPackagesAfter(final Path basePath, final long timestamp) {
        return streamSortedDeltaPackages(basePath)
                .filter(packageDescriptor -> packageDescriptor.getTimestamp() > timestamp);
    }

    public static Stream<PackageDescriptor> streamSortedDeltaPackages(final Path basePath) {
        return streamDeltaPackages(basePath)
                .sorted(PackageManager::sort);
    }

    public static void moveOlderPackageToBackups(final Path basePath) {

        final List<PackageDescriptor> packageDescriptorStream = streamAllPackages(basePath)
                .collect(Collectors.toList());
        final Optional<PackageReaderWrapper> latestDeltaPackage = getLatestDeltaPackage(basePath);

        for (final PackageDescriptor packageDescriptor : packageDescriptorStream) {

            if (latestDeltaPackage.isPresent() && latestDeltaPackage.get().equals(packageDescriptor)) {
                continue;
            }

            final File file = packageDescriptor.getBasePathWithPackageName().toFile();
            FilesystemDal.moveIntoSubdirectory(file, BACKUP_DIRECTORY_NAME);
        }
    }

    public static PackageReaderWrapper getPackageReader(final PackageDescriptor packageDescriptor) {
        return new PackageReaderWrapper(packageDescriptor);
    }

    public static PackageWriterWrapper createDeltaWriter(final PackageDescriptor fullPackage) {
        return new PackageWriterWrapper(new PackageDescriptorForWriting(fullPackage.getTimestamp(),
                PackageScope.DELTA_PACKAGE, fullPackage.getBasePathToPackageLocation()));
    }
}
