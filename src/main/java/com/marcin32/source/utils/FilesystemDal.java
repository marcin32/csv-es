package com.marcin32.source.utils;

import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.file.PackedFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

import static org.apache.commons.io.FileUtils.moveFile;

public class FilesystemDal {

   /* public Set<ReadingPackageDescriptor> listUpdates(final Path baseDirectoryPath) {
        final File baseDirectory = baseDirectoryPath.toFile();

        return Stream.of(Objects.requireNonNull(baseDirectory.list()))
                .map(file -> parse(file, baseDirectoryPath))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<ReadingPackageDescriptor> parse(final String file,
                                                     final Path baseDirectoryPath) {
        final Path path = Paths.get(baseDirectoryPath.toString(), file);
        final File fileOrDir = path.toFile();

        if (fileOrDir.isDirectory()) {
            final String directoryName = fileOrDir.getName();
            if (directoryName.equals("backup")) return Optional.empty();

            final String[] splittedName = directoryName.split("\\-");
            final Long timestamp = Long.parseLong(splittedName[0]);
            final UpdateScope updateScope = UpdateScope.valueOf(splittedName[1]);
            final ReadingPackageDescriptor readingUpdateDescriptor = new ReadingPackageDescriptor(timestamp, path, updateScope,
                    UpdateType.DIRECTORY);
            return Optional.of(readingUpdateDescriptor);
        } else if (fileOrDir.isFile() && fileOrDir.getName().contains(Constants.ARCHIVE_EXTENSION)) {
            final String directoryName = fileOrDir.getName().split("\\.")[0];
            final String[] splittedName = directoryName.split("\\-");
            final Long timestamp = Long.parseLong(splittedName[0]);
            final UpdateScope updateScope = UpdateScope.valueOf(splittedName[1]);
            final ReadingPackageDescriptor readingUpdateDescriptor = new ReadingPackageDescriptor(timestamp, path, updateScope,
                    UpdateType.ARCHIVE);
            return Optional.of(readingUpdateDescriptor);
        }

        return Optional.empty();
    }*/

    public static Optional<TarArchiveInputStream> getTarEntry(final PackedFile packedFile,
                                                              final String fileName) throws IOException {
        final GzipCompressorInputStream gzipCompressorInputStream =
                new GzipCompressorInputStream(new FileInputStream(packedFile.getArchivePath().toFile()));
        TarArchiveInputStream tarInput = new TarArchiveInputStream(gzipCompressorInputStream);
        TarArchiveEntry currentTarEntry = tarInput.getNextTarEntry();
        while (currentTarEntry != null) {
            if (currentTarEntry.getName().equals(fileName))
                return Optional.of(tarInput);
            currentTarEntry = tarInput.getNextTarEntry();
        }

        return Optional.empty();
    }

    public static int countTarEntries(final File tarFile) throws IOException {
        int numberOfFiles = 0;
        final GzipCompressorInputStream gzipCompressorInputStream =
                new GzipCompressorInputStream(new FileInputStream(tarFile));
        TarArchiveInputStream tarInput = new TarArchiveInputStream(gzipCompressorInputStream);
        TarArchiveEntry currentTarEntry = tarInput.getNextTarEntry();
        while (currentTarEntry != null) {
            ++numberOfFiles;
            currentTarEntry = tarInput.getNextTarEntry();
        }

        return numberOfFiles;
    }

    public Optional<File> getFileFromResources(final PackageDescriptor packageDescriptor,
                                               final String databaseName) throws IOException {
        if (packageDescriptor.getPackageType().equals(PackageType.DIRECTORY)) {
            final Path path = Paths.get(packageDescriptor.getBasePathToPackageLocation().toString(), databaseName);
            return Optional.of(path.toFile());
        }

        return Optional.empty();
    }

    public static void compress(final String outputArchive, final File... files) throws IOException {
        try (TarArchiveOutputStream out = getTarArchiveOutputStream(outputArchive)) {
            for (final File file : files) {
                addToArchiveCompression(out, file, "");
            }
        }
    }

    private static TarArchiveOutputStream getTarArchiveOutputStream(final String name) throws IOException {
        final TarArchiveOutputStream taos = new TarArchiveOutputStream(getGzipOutputStream(name));
        // TAR has an 8 gig file limit by default, this gets around that
        taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
        // TAR originally didn't support long file names, so enable the support for it
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        taos.setAddPaxHeadersForNonAsciiNames(true);
        return taos;
    }

    private static GZIPOutputStream getGzipOutputStream(final String name) throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(name);
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bufferedOutputStream);
        return gzipOutputStream;
    }

    private static void addToArchiveCompression(TarArchiveOutputStream out, File file, String dir) throws IOException {
        String entry = dir + File.separator + file.getName();
        if (file.isFile()) {
            out.putArchiveEntry(new TarArchiveEntry(file, entry));
            try (FileInputStream in = new FileInputStream(file)) {
                IOUtils.copy(in, out);
            }
            out.closeArchiveEntry();
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    addToArchiveCompression(out, child, entry);
                }
            }
        } else {
            System.out.println(file.getName() + " is not supported");
        }
    }

    public static File[] listFiles(final Path path) {
        return path.toFile().listFiles();
    }

    public void copyFile(final Path source, final Path destination) throws IOException {
        FileUtils.copyFile(source.toFile(), destination.toFile());
    }

    public void createDirectoryIfNotExists(final Path basePath, final String directoryName) {
        final File[] files = basePath.toFile().listFiles();

        if (files != null) { //some JVMs return null for empty dirs
            for (final File f : files) {
                if (f.isDirectory() && f.getName().equals(directoryName)) {
                    break;
                }
            }
        }

        createDirectory(basePath, directoryName);
    }

    public void createDirectory(final Path basePath, final String directoryName) {
        basePath.resolve(directoryName).toFile().mkdirs();
    }

    public static void deleteFolder(final File folder) {
        final File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void moveIntoSubdirectory(final File source, final String backupDirectoryName) {
        final Path parent = source.toPath().getParent();
        final File target = parent.resolve(backupDirectoryName).resolve(source.getName()).toFile();
        try {
            moveFile(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sanitizeFileName(final String name) {
        if (null == name) {
            return "";
        }

        return name.replaceAll("[\u0000-\u001f<>:\"/\\\\|?*\u007f]+", "").trim();
    }

    public static File getFileFromResources(final String archiveName) {
        final URL url = ClassLoader.getSystemResource(archiveName);
        return new File(url.getFile());
    }

    private FilesystemDal() {

    }
}
