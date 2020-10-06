package com.marcin32.source.model.csv;

import com.marcin32.source.base.Constants;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.utils.ShaUtil;

import java.util.Optional;

public class ChangedEntityAdapter extends AbstractFormatAdapter<CsvEntry> {
    @Override
    public Optional<CsvEntry> deserializeCsvLine(final String[] line) {

        final String entityUuid = line[0];
        final Long timeStamp = Long.parseLong(line[1]);
        final String entityContentShaHash = line[2];
        final String entityContent = line[3];
        try {
            return Optional.of(new CsvEntry(timeStamp, entityUuid, entityContentShaHash, entityContent));
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    protected String serializeContentInternal(final String... parts) {
        //final String content = gson.toJson(entity);

        final String uuid = parts[0];
        final String serializedEntity = parts[1];

        return uuid + Constants.CSV_SEPARATOR_FOR_WRITING +
                System.currentTimeMillis() +
                Constants.CSV_SEPARATOR_FOR_WRITING +
                ShaUtil.shaHash(serializedEntity) +
                Constants.CSV_SEPARATOR_FOR_WRITING +
                serializedEntity;
    }

    @Override
    int getDesiredPartsLength() {
        return 2;
    }
}
