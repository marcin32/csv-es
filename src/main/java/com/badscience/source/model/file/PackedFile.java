package com.badscience.source.model.file;

import com.badscience.source.utils.FilesystemDal;
import lombok.Getter;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
public class PackedFile extends AbstractFile {

    private final Path archivePath;

    public PackedFile(final String fileName, final Path archivePath) {
        super(fileName);
        this.archivePath = archivePath;
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
}
