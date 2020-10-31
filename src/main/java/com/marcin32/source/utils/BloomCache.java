package com.marcin32.source.utils;

import com.github.wxisme.bloomfilter.bitset.JavaBitSet;
import com.github.wxisme.bloomfilter.common.BloomFilter;
import com.marcin32.source.model.file.AbstractFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BloomCache {

    private final Map<AbstractFile, BloomFilter<String>> databaseToCache = new HashMap<>();

    private final Map<AbstractFile, Set<String>> databaseToHash = new HashMap<>();

    public synchronized void populateCache(final AbstractFile table, final String contentHash) {
        if (!databaseToCache.containsKey(table)) {
            final BloomFilter<String> filter = new BloomFilter<>(0.0001, (int) table.getNumberOfEntries());
            filter.bind(new JavaBitSet());
            databaseToCache.put(table, filter);
            databaseToHash.put(table, new HashSet<>());
        }

        databaseToCache.get(table).add(contentHash);
        databaseToHash.get(table).add(contentHash);
    }

    public synchronized boolean mightContain(final AbstractFile table, final String contentHash) {
        if (!databaseToCache.containsKey(table))
            return true;

        final Boolean contains1 = databaseToCache.get(table).contains(contentHash);
        final Boolean contains2 = databaseToHash.get(table).contains(contentHash);
        //assert(contains1.equals(contains2));
        return contains1;
    }

    public boolean hasDatabasePopulated(final AbstractFile tableName) {
        return databaseToCache.containsKey(tableName);
    }
}
