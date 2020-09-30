package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.AbstractFile;
import com.marcin32.source.utils.BloomCache;

import java.io.IOException;
import java.util.stream.Stream;

public class CachedTableReader extends AbstractTableReader {

    private final TableReader tableReader = new TableReader();

    private final BloomCache bloomCache = new BloomCache();

    @Override
    public <ENTITYTYPE> Stream<SourceEntry<ENTITYTYPE>> readEntities(final AbstractFile file,
                                                                     final Class<ENTITYTYPE> entitytype) throws IOException {
        return tableReader.readEntities(file, entitytype);
    }

    @Override
    public Stream<String> readUuidsOfTimestampedEntities(final AbstractFile file) {
        return tableReader.readUuidsOfTimestampedEntities(file);
    }

    @Override
    public <ENTITYTYPE> boolean checkWhetherTableContainsEntity(final String entityContentHash,
                                                                final Class<ENTITYTYPE> entity,
                                                                final AbstractFile file) {

        if (!bloomCache.hasDatabasePopulated(file)) {
            synchronized (this) {
                if (!bloomCache.hasDatabasePopulated(file)) {
                    System.out.println("Populating cache for file: " + file.getFileName());
                    try {
                        readEntities(file, entity)
                                .peek(element -> System.out.println(element.getShaContentHash()))
                                .forEach(element -> bloomCache.populateCache(file, element.getShaContentHash()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!bloomCache.shouldCheck(file, entityContentHash))
            return true;

        try {
            return readEntities(file, entity)
                    .anyMatch(abstractCsvEntity -> abstractCsvEntity.getShaContentHash().equals(entityContentHash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
