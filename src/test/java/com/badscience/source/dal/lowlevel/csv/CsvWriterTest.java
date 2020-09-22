package com.badscience.source.dal.lowlevel.csv;

import com.badscience.source.writing.WritingDescriptorsDal;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class CsvWriterTest {

    private final WritingDescriptorsDal writingDescriptorsDal = new WritingDescriptorsDal();

    private static final String TABLE_NAME = "testEntity";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldWriteFullUpdateAndCreateItsCopyAsDeltaWhenItIsTheFirstOne() throws IOException {
        /*final Path path = temporaryFolder.getRoot().toPath();
        final WritingPackageDescriptor descriptorForWriting = writingDescriptorsDal.getDescriptorForWriting(path);

        final TestEntity1 testEntity1 = new TestEntity1("a");
        final TestEntity1 testEntity2 = new TestEntity1("b");
        final TestEntity1 testEntity3 = new TestEntity1("c");

        descriptorForWriting.storeEntity("a", TABLE_NAME, testEntity1);
        descriptorForWriting.storeEntity("b", TABLE_NAME, testEntity2);
        descriptorForWriting.storeEntity("c", TABLE_NAME, testEntity3);

        descriptorForWriting.saveUpdateAndFinish();

        final ReadingDescriptorsDal readingDescriptorsDal = new ReadingDescriptorsDal();
        final Optional<ReadingPackageDescriptor> lastDelta = readingDescriptorsDal.getLastDelta(path);
        assertTrue(lastDelta.isPresent());

        final Set<TestEntity1> testTable1 = lastDelta.get().readEntities(TABLE_NAME, TestEntity1.class)
                .collect(Collectors.toSet());

        final HashSet<TestEntity1> testEntitySet2 = new HashSet<>(List.of(testEntity1, testEntity2, testEntity3));
        assertEquals(testTable1, testEntitySet2);*/
    }

    @Test
    public void shouldWriteFullUpdateAndCreateDeltaWhenAnotherFullUpdateHasBeenAlreadyPresent() {

    }


    @Test
    public void shouldSaveAndReadEntity() throws IOException {
        /*final String testContent = "Test Content XD";
        final String testEntityUuid = "uuid1";
        final String databaseName = "testDatabaseName.txt";
        final Path temporaryPath = folder.newFolder().toPath();
        final WritingPackageDescriptor writingUpdateDescriptor = new WritingPackageDescriptor(1234L, temporaryPath,
                UpdateScope.FULL_UPDATE, UpdateType.DIRECTORY);
        final FileDescriptors fileDescriptors = new FileDescriptors();
        final CsvWriter csvWriter = new CsvWriter(writingUpdateDescriptor, fileDescriptors);

        final TestEntity1 testEntity1 = new TestEntity1();
        testEntity1.setContent(testContent);

        csvWriter.saveEntity(testEntityUuid, databaseName, testEntity1);
        fileDescriptors.closeAllFiles();

        final TableReader entityReader = new TableReader();

        final Path path = Paths.get(temporaryPath.toString(),
                //updateDatabaseInfo.getTimestamp() + "-" + updateDatabaseInfo.getUpdateScope().toString(),
                databaseName);

        try(final Stream<AbstractSourceEntity<TestEntity1>> abstractSourceEntityStream =
                    entityReader.readEntities(path.toFile(), TestEntity1.class)) {
            final Optional<AbstractSourceEntity<TestEntity1>> firstEntity = abstractSourceEntityStream
                    .findFirst();

            assertTrue(firstEntity.isPresent());
            assertEquals(testEntityUuid, firstEntity.get().getUuid());
            assertEquals(testContent, firstEntity.get().getEntity().getContent());
        }*/
    }
}
