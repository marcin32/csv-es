package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.base.Constants;
import com.marcin32.source.dal.lowlevel.file.FileReader;
import com.marcin32.source.model.file.AbstractFile;

import java.util.stream.Stream;

public class CsvReader {

    private final static FileReader fileReader = new FileReader();

    public Stream<String[]> readCsv(final AbstractFile file) {

        return fileReader.readFile(file)
                .filter(line -> filterEmptyLines(line, file))
                .map(this::splitLine);
    }

    private String[] splitLine(final String line) {
        return line.split(Constants.CSV_SEPARATOR_REGEX_PATTERN);
    }

    private boolean filterEmptyLines(final String line, final AbstractFile file) {
        if(line == null || line.isEmpty()) {
            System.err.printf("Empty line in file: %n", file.getFileName());
            return false;
        }
        return true;
    }


}
