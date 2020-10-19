package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.model.PackedTableMetadata;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackageWriterTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldWriteFullUpdateAndCreateItsCopyAsDeltaWhenItIsTheFirstOne() throws IOException {
        final File tempDir = temporaryFolder.newFolder();
        final PackageWriter packageWriter = new PackageWriter();
        final long timestamp = System.currentTimeMillis();
        final PackageDescriptorForWriting packageDescriptor = new PackageDescriptorForWriting(timestamp, PackageScope.FULL_PACKAGE,
                tempDir.toPath());

        final TestEntity1 testEntity1 = new TestEntity1("a", "a_content");
        final TestEntity1 testEntity2 = new TestEntity1("b", "b_content");
        final TestEntity1 testEntity3 = new TestEntity1("c", "c_content");

        packageWriter.storeEntity("a", testEntity1, packageDescriptor);
        packageWriter.storeEntity("b", testEntity2, packageDescriptor);
        packageWriter.storeEntity("c", testEntity3, packageDescriptor);

        packageWriter.saveMetadata(packageDescriptor);
        packageWriter.closePackage(packageDescriptor);

        final PackageDescriptor packageForReading = new PackageDescriptor(timestamp, PackageScope.FULL_PACKAGE,
                PackageType.ARCHIVE, tempDir.toPath());

        final Path basePathWithPackageName = packageForReading.getBasePathWithPackageName();
        assertTrue(basePathWithPackageName.toFile().exists());

        final PackageReader packageReader = new PackageReader();
        final long numberOfEntitiesInMetadata = packageReader.getPackageMetadata(packageForReading)
                .filter(meta -> meta.getClassName().equals(TestEntity1.class.getSimpleName() + Constants.TABLE_EXTENSION))
                .mapToLong(PackedTableMetadata::getNumberOfEntities)
                .findFirst()
                .getAsLong();
        final long countedEntities = packageReader.readEntities(TestEntity1.class, packageForReading)
                .count();

        assertEquals(countedEntities, numberOfEntitiesInMetadata);

//        final Set<TestEntity1> testTable1 = lastDelta.get().readEntities(TABLE_NAME, TestEntity1.class)
//                .collect(Collectors.toSet());
//
//        final HashSet<TestEntity1> testEntitySet2 = new HashSet<>(List.of(testEntity1, testEntity2, testEntity3));
//        assertEquals(testTable1, testEntitySet2);
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
