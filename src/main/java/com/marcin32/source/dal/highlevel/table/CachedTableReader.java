package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.csv.ITableFormatAdapter;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.utils.BloomCache;

import java.util.stream.Stream;

public class CachedTableReader extends AbstractTableReader {

    private final TableReader tableReader = new TableReader();

    private final BloomCache bloomCache = new BloomCache();

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
    public boolean checkWhetherTableContainsHash(final String entityContentHash,
                                                 final AbstractFile file) {
        if (!bloomCache.hasDatabasePopulated(file)) {
            synchronized (this) {
                if (!bloomCache.hasDatabasePopulated(file)) {
                    readEntities(file, CHANGED_ENTITY_FORMAT_ADAPTER)
                            .forEach(element -> bloomCache.populateCache(file, entityContentHash));

                }
            }
        }
        if (bloomCache.mightContain(file, entityContentHash)) {
            return readEntities(file, CHANGED_ENTITY_FORMAT_ADAPTER)
                    .anyMatch(abstractCsvEntity -> abstractCsvEntity.getShaContentHash().equals(entityContentHash));
        }
        return false;
    }

//    public <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final Class<ENTITYTYPE> entity,
//                                                                final AbstractFile file) {
//
//        if (!bloomCache.hasDatabasePopulated(file)) {
//            synchronized (this) {
//                if (!bloomCache.hasDatabasePopulated(file)) {
//                    readEntities(file, entity)
//                            .forEach(element -> bloomCache.populateCache(file, element.getShaContentHash()));
//
//                }
//            }
//        }
//        if (bloomCache.mightContain(file, entityContentHash)) {
//            return readEntities(file, entity)
//                    .anyMatch(abstractCsvEntity -> abstractCsvEntity.getShaContentHash().equals(entityContentHash));
//        }
//        return false;
//    }
}
