package com.marcin32.source.model.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public abstract class AbstractFile {

    private final String fileName;

    public abstract Stream<String> readFile() throws IllegalAccessException, IOException;

    public abstract long getNumberOfLines();
}
