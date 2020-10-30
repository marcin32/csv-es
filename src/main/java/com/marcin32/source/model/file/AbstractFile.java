package com.marcin32.source.model.file;

import java.io.IOException;
import java.util.stream.Stream;

public abstract class AbstractFile {

    private final String fileName;

    public AbstractFile(String fileName) {
        this.fileName = fileName;
    }

    public abstract Stream<String> readFile() throws IllegalAccessException, IOException;

    public abstract long getNumberOfEntries();

    public String getFileName() {
        return this.fileName;
    }
}
