package com.marcin32.source.dal.highlevel.table;

import com.google.gson.Gson;
import com.marcin32.source.dal.lowlevel.csv.CsvReader;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.csv.ChangedEntityAdapter;
import com.marcin32.source.model.csv.UnchangedEntityAdapter;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.utils.ShaUtil;

import java.util.stream.Stream;

public abstract class AbstractTableReader implements ITableReader {

    protected static final UnchangedEntityAdapter UNCHANGED_ENTITY_FORMAT_ADAPTER = new UnchangedEntityAdapter();

    protected static final ChangedEntityAdapter CHANGED_ENTITY_FORMAT_ADAPTER = new ChangedEntityAdapter();

    final static Gson gson = new Gson();

    protected final static CsvReader csvReader = new CsvReader();

    @Override
    public <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final ENTITYTYPE entity,
                                                                final AbstractFile file) {
        final String shaHash = getEntityHash(entity);
        return checkWhetherTableContainsHash(shaHash, file);
    }

    <ENTITYTYPE> String getEntityHash(final ENTITYTYPE entity) {
        return ShaUtil.shaHash(gson.toJson(entity));
    }

    @Override
    public Stream<CsvEntry> readRawCsvEntries(final AbstractFile file) {
        return readEntities(file, CHANGED_ENTITY_FORMAT_ADAPTER);
    }
}
