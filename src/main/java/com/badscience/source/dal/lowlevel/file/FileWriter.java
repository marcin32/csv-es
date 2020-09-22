package com.badscience.source.dal.lowlevel.file;

import com.badscience.source.model.file.RawFile;

import java.io.IOException;

public class FileWriter {

    public synchronized void appendFile(final String line,
                                        final RawFile rawFile) {

        try {
            rawFile.appendLine(line);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
