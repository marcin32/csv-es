package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.ITableFormatAdapter;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.utils.BloomCache;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CachedTableReader extends AbstractTableReader {

    private final TableReader tableReader = new TableReader();

    private final BloomCache bloomCache = new BloomCache();

    private final Map<String, Long> stats = new HashMap<>();

    @Override
    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                                     final Class<ENTITYTYPE> entitytype) {
        return tableReader.readEntities(file, entitytype);
    }

    @Override
    public <TARGET_TYPE> Stream<TARGET_TYPE> readEntities(final AbstractFile file,
                                                          final ITableFormatAdapter<TARGET_TYPE> tableFormatAdapter) {
        return tableReader.readEntities(file, tableFormatAdapter);
    }

    @Override
    public Stream<String> readUuidsOfTimestampedEntities(final AbstractFile file) {
        return tableReader.readUuidsOfTimestampedEntities(file);
    }

    @Override
    public boolean checkWhetherTableMightContainHash(final String entityContentHash,
                                                     final AbstractFile file) {
        if (!bloomCache.hasDatabasePopulated(file)) {
            synchronized (this) {
                if (!bloomCache.hasDatabasePopulated(file)) {
                    readRawCsvEntries(file)
                            .forEach(element -> bloomCache.populateCache(file, element.getShaContentHash()));

                }
            }
        }
        return bloomCache.mightContain(file, entityContentHash);
    }

    private void stats(final String str) {
        if(stats.containsKey(str)) {
            final Long aLong = stats.get(str);
            stats.put(str, aLong+1);
        } else {
            stats.put(str, 1L);
        }
    }
}
