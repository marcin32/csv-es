package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TableReaderTest {

    @Test
    public void shouldReadTestFile() throws IOException {

        final String fileName = "testDatabase1.csv";
        final TableReader tableReader = new TableReader();
        final File file = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(fileName, file.toPath().getParent());

        long sum = 0;
        try (final Stream<SourceEntry<CsvEntry>> stringStream = tableReader.readEntities(rawFile, CsvEntry.class)) {
            sum = stringStream
                    .mapToLong(CsvEntry::getTimestamp)
                    .sum();
        }
        try (final Stream<SourceEntry<TestEntity1>> stringStream = tableReader.readEntities(rawFile, TestEntity1.class)) {
            final Optional<TestEntity1> first = stringStream
                    .map(SourceEntry::getEntity)
                    .findFirst();
            assertTrue(first.isPresent());
            assertEquals("testContent1", first.get().getContent());
        }

        assertEquals("Should read 1 line", 123456, sum);
    }

    @Test
    public void shouldReadPackedFileLineAfterLine() throws IOException {

        final String fileName = "TestEntity1.csv";
        final String archiveName = "updates1/2345-DELTA_PACKAGE.tar.gz";
        final TableReader tableReader = new TableReader();
        final File archive = FilesystemDal.getFileFromResources(archiveName);
        final PackedFile packedFile = new PackedFile(fileName, 1, archive.toPath());

        long sum = 0;
        try (final Stream<SourceEntry<CsvEntry>> stringStream = tableReader.readEntities(packedFile, CsvEntry.class)) {
            sum = stringStream
                    .mapToLong(CsvEntry::getTimestamp)
                    .sum();
        }
        try (final Stream<SourceEntry<TestEntity1>> stringStream = tableReader.readEntities(packedFile, TestEntity1.class)) {
            final Optional<TestEntity1> first = stringStream
                    .map(SourceEntry::getEntity)
                    .findFirst();
            assertTrue(first.isPresent());
            assertEquals("testContent1", first.get().getContent());
        }

        assertEquals("Should read 1 line", 123456, sum);
    }

    @Test
    public void shouldReadUnpackedFileLineAfterLine() throws IOException {

        final String fileName = "updates1/1234-FULL_PACKAGE/TestEntity1.csv";
        final TableReader tableReader = new TableReader();
        final File file = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(file.getName(), file.toPath().getParent());

        long sum = 0;
        try (final Stream<SourceEntry<TestEntity1>> stringStream = tableReader.readEntities(rawFile, TestEntity1.class)) {
            sum = stringStream
                    .mapToLong(CsvEntry::getTimestamp)
                    .sum();
            assertEquals("Should read 1 line", 123456, sum);
        }
        try (final Stream<SourceEntry<TestEntity1>> stringStream = tableReader.readEntities(rawFile, TestEntity1.class)) {
            final Optional<TestEntity1> first = stringStream
                    .map(SourceEntry::getEntity)
                    .findFirst();
            assertTrue(first.isPresent());
            assertEquals("testContent1", first.get().getContent());
        }
    }

    @Test
    public void shouldFindEntityInTable() {
        final String fileName = "updates1/1234-FULL_PACKAGE/TestEntity1.csv";
        final TableReader tableReader = new TableReader();
        final File file = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(file.getName(), file.toPath().getParent());
        final TestEntity1 testEntity1 = new TestEntity1("entityId1", "testContent1");

        final boolean result = tableReader.checkWhetherTableContainsEntity(testEntity1, rawFile);
        assertTrue(result);
    }

    @Test
    public void shouldNotFindEntityInTable() {
        final String fileName = "updates1/1234-FULL_PACKAGE/TestEntity1.csv";
        final TableReader tableReader = new TableReader();
        final File file = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(file.getName(), file.toPath().getParent());
        final TestEntity1 testEntity1 = new TestEntity1("entityId2", "testContent2");

        final boolean result = tableReader.checkWhetherTableContainsEntity(testEntity1, rawFile);
        assertFalse(result);
    }
}
