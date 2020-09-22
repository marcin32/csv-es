package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.base.Constants;
import com.marcin32.source.dal.lowlevel.file.FileWriter;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.ShaUtil;

public class CsvWriter extends AbstractCsvDal {

    private final FileWriter fileWriter = new FileWriter();

    private final RawFile rawFile;

    public CsvWriter(final RawFile rawFile) {
        this.rawFile = rawFile;
    }

    public <ENTITYTYPE> void saveEntity(final String uuid,
                                        final ENTITYTYPE entity) {

        final String line = prepareContent(uuid, entity);
        fileWriter.appendFile(line, rawFile);
    }

    private <ENTITYTYPE> String prepareContent(final String uuid,
                                               final ENTITYTYPE entity) {
        final String content = gson.toJson(entity);

        return uuid + Constants.CSV_SEPARATOR_FOR_WRITING +
                System.currentTimeMillis() +
                Constants.CSV_SEPARATOR_FOR_WRITING +
                ShaUtil.shaHash(content) +
                Constants.CSV_SEPARATOR_FOR_WRITING +
                content;
    }


}
