package com.marcin32.source.dal.lowlevel.file;

import com.marcin32.source.model.file.RawFile;

import java.io.IOException;

public class FileWriter {

    public void appendFile(final String line,
                           final RawFile rawFile) {

        try {
            rawFile.appendLine(line);
        } catch (final IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeFile(final RawFile rawFile) {
        try {
            rawFile.close();
        } catch (final IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
