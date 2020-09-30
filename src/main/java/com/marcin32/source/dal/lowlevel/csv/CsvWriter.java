package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.base.Constants;
import com.marcin32.source.dal.lowlevel.file.FileWriter;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.ShaUtil;

public class CsvWriter {

    private static final FileWriter fileWriter = new FileWriter();

    public CsvWriter() {
    }

    public <ENTITYTYPE> void saveEntity(final RawFile rawFile,
                                        final String uuid,
                                        final String serializedEntity) {

        final String line = prepareContent(uuid, serializedEntity);
        fileWriter.appendFile(line, rawFile);
    }

    private <ENTITYTYPE> String prepareContent(final String uuid,
                                               final String serializedEntity) {
        //final String content = gson.toJson(entity);

        return uuid + Constants.CSV_SEPARATOR_FOR_WRITING +
                System.currentTimeMillis() +
                Constants.CSV_SEPARATOR_FOR_WRITING +
                ShaUtil.shaHash(serializedEntity) +
                Constants.CSV_SEPARATOR_FOR_WRITING +
                serializedEntity;
    }


}
