package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.dal.lowlevel.file.FileWriter;
import com.marcin32.source.model.csv.ITableFormatAdapter;
import com.marcin32.source.model.file.RawFile;

public class CsvWriter {

    private static final FileWriter fileWriter = new FileWriter();

    public CsvWriter() {
    }

    public <ENTITYTYPE> void saveEntity(final RawFile rawFile,
                                        final ITableFormatAdapter<ENTITYTYPE> formatAdapter,
                                        final String... parts) {

        final String line = formatAdapter.serializeContent(parts);
        fileWriter.appendFile(line, rawFile);
    }

    public <ENTITYTYPE> void saveEntity(final RawFile rawFile,
                                        final ITableFormatAdapter<ENTITYTYPE> formatAdapter,
                                        final ENTITYTYPE entity) {

        final String line = formatAdapter.serializeContent(entity);
        fileWriter.appendFile(line, rawFile);
    }

    public void closeCsvFile(final RawFile rawFile) {
        fileWriter.closeFile(rawFile);
    }
}
