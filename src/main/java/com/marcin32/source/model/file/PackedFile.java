package com.marcin32.source.model.file;

import com.marcin32.source.utils.FilesystemDal;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class PackedFile extends AbstractFile {

    private final Path archivePath;

    private final long numberOfLines;

    public PackedFile(final String fileName, final long numberOfLines, final Path archivePath) {
        super(fileName);
        this.archivePath = archivePath;
        this.numberOfLines = numberOfLines;
    }

    @Override
    public Stream<String> readFile() throws IOException {
        final Optional<TarArchiveInputStream> tarEntry = FilesystemDal.getTarEntry(this, getFileName());
        if (tarEntry.isPresent()) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(tarEntry.get()));
            return br.lines();
        }
        return Stream.empty();
    }

    @Override
    public long getNumberOfEntries() {
        return this.numberOfLines;
    }

    public Path getArchivePath() {
        return this.archivePath;
    }

    public long getNumberOfLines() {
        return this.numberOfLines;
    }
}
