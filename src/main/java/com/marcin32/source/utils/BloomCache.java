package com.marcin32.source.utils;

import com.github.wxisme.bloomfilter.bitset.JavaBitSet;
import com.github.wxisme.bloomfilter.common.BloomFilter;
import com.marcin32.source.model.file.AbstractFile;

import java.util.HashMap;
import java.util.Map;

public class BloomCache {

    private final Map<AbstractFile, BloomFilter<String>> databaseToCache = new HashMap<>();

    public synchronized void populateCache(final AbstractFile table, final String contentHash) {
        if (!databaseToCache.containsKey(table)) {
            final BloomFilter<String> filter = new BloomFilter<>(0.0001, (int) table.getNumberOfEntries());
            filter.bind(new JavaBitSet());
            databaseToCache.put(table, filter);
        }

        databaseToCache.get(table).add(contentHash);
    }

    public synchronized boolean mightContain(final AbstractFile table, final String contentHash) {
        if (!databaseToCache.containsKey(table))
            return true;

        return databaseToCache.get(table).contains(contentHash);
    }

    public boolean hasDatabasePopulated(final AbstractFile tableName) {
        return databaseToCache.containsKey(tableName);
    }
}
