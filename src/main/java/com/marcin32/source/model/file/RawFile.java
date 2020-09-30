package com.marcin32.source.model.file;

import com.marcin32.source.utils.FilesystemDal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class RawFile extends AbstractFile {

    private final Path directoryPath;
    private final LongAdder totalNumberOfLines = new LongAdder();
    private boolean closed = false;
    private boolean dirty = false;
    private BufferedWriter writer;


    public RawFile(final String fileName, final Path directoryPath) {
        super(fileName);
        this.directoryPath = directoryPath;
    }

    BufferedWriter getWriter() throws IOException, IllegalAccessException {
        if (closed) {
            throw new IllegalAccessException(String.format("File %s already closed!", this.getFileName()));
        }

        if (writer == null) {
            writer = Files.newBufferedWriter(getDirectoryPathWithFileName(), StandardCharsets.UTF_8, StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        }

        return writer;
    }

    public Path getDirectoryPath() {
        return directoryPath;
    }

    public Path getDirectoryPathWithFileName() {
        final String fileName = FilesystemDal.sanitizeFileName(getFileName());
        return directoryPath.resolve(fileName);
    }

    public long getNumberOfLines() {
        return totalNumberOfLines.sum();
    }

    public void appendLine(final String line) throws IOException, IllegalAccessException {

        final BufferedWriter writerInstance = getWriter();

        writerInstance.write(line);
        writerInstance.newLine(); // why newline is being written after "next" record has been stored?
        // is this a "synchronized" bug?
        writerInstance.flush();
        totalNumberOfLines.increment();
        dirty = true;
    }

    public void close() throws IOException, IllegalAccessException {
        final BufferedWriter writerInstance = getWriter();
        writerInstance.close();
        closed = true;
        dirty = false;
    }

    @Override
    public Stream<String> readFile() throws IllegalAccessException, IOException {

        if (dirty && !closed) {
            throw new IllegalAccessException(String.format("File %s is not yet closed!", this.getFileName()));
        }

        final Path directoryPathWithFileName = getDirectoryPathWithFileName();
        if (Files.exists(directoryPathWithFileName))
            return Files.lines(directoryPathWithFileName, StandardCharsets.UTF_8);
        else return Stream.empty();
    }
}
