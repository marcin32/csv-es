package com.marcin32.source.utils;

import com.marcin32.source.model.file.PackedFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BloomCacheTest {

    BloomCache bloomCache;

    @Mock
    PackedFile abstractFile;

    @Before
    public void setUp() {
        when(abstractFile.getNumberOfEntries()).thenReturn(1L);
        bloomCache = new BloomCache();
    }

    @Test
    public void shouldPopulateCache() {

        bloomCache.populateCache(abstractFile, "sha1");
    }

    @Test
    public void shouldHaveDatabasePopulated() {

        bloomCache.populateCache(abstractFile, "sha1");
        assertTrue(bloomCache.hasDatabasePopulated(abstractFile));
    }

    @Test
    public void shouldFindEntityByHash() {

        final String sha1 = "sha1";
        bloomCache.populateCache(abstractFile, sha1);
        assertTrue(bloomCache.hasDatabasePopulated(abstractFile));
        assertTrue(bloomCache.mightContain(abstractFile, sha1));
    }

    @Test
    public void shouldNotFindEntityByHash() {

        bloomCache.populateCache(abstractFile, "sha1");
        assertTrue(bloomCache.hasDatabasePopulated(abstractFile));
        assertFalse(bloomCache.mightContain(abstractFile, "sha2"));
    }
}