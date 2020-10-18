package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.RawFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldWriteTable() throws IOException {
        final String content = "tableTestContentWriter";
        final String uuid = "uuid1";
        final TestEntity1 testEntity1 = new TestEntity1(uuid, content);
        final String fileName = "TestTable";
        final File temporaryFolder = folder.newFolder();

        final TableWriter tableWriter = new TableWriter();
        final RawFile rawFile = new RawFile(fileName, temporaryFolder.toPath());

        tableWriter.writeEntity(rawFile, uuid, testEntity1);
        tableWriter.closeTable(rawFile);
        assertEquals(1, rawFile.getNumberOfEntries());

        try (final Stream<SourceEntry<TestEntity1>> stringStream = tableWriter.readEntities(rawFile, TestEntity1.class)) {

            final Optional<SourceEntry<TestEntity1>> first = stringStream.findFirst();
            assertTrue(first.isPresent());

            assertEquals(content, first.get().getEntity().getContent());
            assertEquals(uuid, first.get().getEntity().getUuid());
        }
    }

    @Test
    public void shouldWriteTimestampForEntity() throws IOException {
        final String uuid = "uuid1";
        final String fileName = "TestTable";
        final File temporaryFolder = folder.newFolder();

        final TableWriter tableWriter = new TableWriter();
        final RawFile rawFile = new RawFile(fileName, temporaryFolder.toPath());

        tableWriter.writeTimestampForEntity(rawFile, uuid);
        tableWriter.closeTable(rawFile);
        assertEquals(1, rawFile.getNumberOfEntries());

        try (final Stream<String> stringStream = tableWriter.readUuidsOfTimestampedEntities(rawFile)) {

            final Optional<String> first = stringStream.findFirst();
            assertTrue(first.isPresent());

            assertEquals(uuid, first.get());
        }
    }
}
