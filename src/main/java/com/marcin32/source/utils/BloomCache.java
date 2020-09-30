package com.marcin32.source.utils;

import com.marcin32.source.model.file.AbstractFile;

import java.util.HashMap;
import java.util.Map;

public class BloomCache {

    private final Map<AbstractFile, BloomFilter> databaseToCache = new HashMap<>();

    public synchronized void populateCache(final AbstractFile database, final String contentHash) {
        if (!databaseToCache.containsKey(database)) {
            final long numberOfLines = database.getNumberOfLines();
            databaseToCache.put(database, new BloomFilter((int) numberOfLines, (int) numberOfLines * 10000));
        }

        databaseToCache.get(database).add(contentHash);
    }

    public synchronized boolean shouldCheck(final AbstractFile database, final String contentHash) {
        if (!databaseToCache.containsKey(database))
            return true;

        return databaseToCache.get(database).contains(contentHash);
    }

    public boolean hasDatabasePopulated(final AbstractFile databaseName) {
        return databaseToCache.containsKey(databaseName);
    }
}
