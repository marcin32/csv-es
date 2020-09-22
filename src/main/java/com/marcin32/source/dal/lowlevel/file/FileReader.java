package com.marcin32.source.dal.lowlevel.file;

import com.marcin32.source.model.file.AbstractFile;

import java.io.IOException;
import java.util.stream.Stream;

public class FileReader {

    public Stream<String> readFile(final AbstractFile file) {
        try {
            return file.readFile();
        } catch (final IllegalAccessException | IOException th) {
            th.printStackTrace();
        }
        return Stream.empty();
    }
}
