package com.marcin32.source.dal.lowlevel.file;

import com.marcin32.source.model.file.RawFile;

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

    public synchronized void closeFile(final RawFile rawFile)  {
        try {
            rawFile.close();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
