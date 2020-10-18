package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.utils.FilesystemDal;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CachedTableReaderTest {

    @Test
    public void shouldFindEntity() {

        final String fileName = "TestEntity1.csv";
        final String archiveName = "updates1/2345-DELTA_PACKAGE.tar.gz";
        final File archive = FilesystemDal.getFileFromResources(archiveName);
        final PackedFile packedFile = new PackedFile(fileName, 1, archive.toPath());
        final CachedTableReader cachedTableReader = new CachedTableReader();
        final TestEntity1 testEntity1 = new TestEntity1("entityId1", "testContent1");

        boolean result = cachedTableReader.checkWhetherTableContainsEntity(testEntity1, packedFile);
        assertTrue(result);

    }

    @Test
    public void shouldNotFindEntity() {

        final String fileName = "TestEntity1.csv";
        final String archiveName = "updates1/2345-DELTA_PACKAGE.tar.gz";
        final File archive = FilesystemDal.getFileFromResources(archiveName);
        final PackedFile packedFile = new PackedFile(fileName, 1, archive.toPath());
        final CachedTableReader cachedTableReader = new CachedTableReader();
        final TestEntity1 testEntity1 = new TestEntity1("qweert1", "aasdsasdsasad");

        boolean result = cachedTableReader.checkWhetherTableContainsEntity(testEntity1, packedFile);
        assertFalse(result);
    }
}