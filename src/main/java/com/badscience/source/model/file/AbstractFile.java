package com.badscience.source.model.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public abstract class AbstractFile {

    private String fileName;

    public abstract Stream<String> readFile() throws IllegalAccessException, IOException;
}
