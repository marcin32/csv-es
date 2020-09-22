package com.badscience.source.dal.lowlevel.csv;

import com.badscience.source.base.Constants;
import com.badscience.source.dal.lowlevel.file.FileReader;
import com.badscience.source.model.CsvEntry;
import com.badscience.source.model.file.AbstractFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class CsvReader extends AbstractCsvDal {

    private final static FileReader fileReader = new FileReader();

    public Stream<CsvEntry> readCsv(final AbstractFile file) throws IOException {

        return fileReader.readFile(file)
                .map(this::convertCsvLine)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<CsvEntry> convertCsvLine(final String line) {
        final String[] splittedEntity = line.split(Constants.CSV_SEPARATOR_REGEX_PATTERN);

        final String entityUuid = splittedEntity[0];
        final Long timeStamp = Long.parseLong(splittedEntity[1]);
        final String entityContentShaHash = splittedEntity[2];
        final String entityContent = splittedEntity[3];
        try {
            return Optional.of(new CsvEntry(timeStamp, entityUuid, entityContentShaHash, entityContent));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
}
