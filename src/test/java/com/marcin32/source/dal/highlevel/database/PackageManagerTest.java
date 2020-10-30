package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.model.PackedTableMetadata;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackageManagerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldCreateDeltaPackage() throws IOException {
        final File file = temporaryFolder.newFolder();

        final TestEntity1 testEntity1 = new TestEntity1("uuid1", "content1");
        final TestEntity1 testEntity2 = new TestEntity1("uuid2", "content2");
        final TestEntity1 testEntity3 = new TestEntity1("uuid3", "content3");

        try (final PackageWriterWrapper newPackage = PackageManager.createNewPackage(file.toPath())) {

            newPackage.storeEntity("uuid1", testEntity1);
            newPackage.storeEntity("uuid2", testEntity2);
            newPackage.storeEntity("uuid3", testEntity3);
        }

        final TestEntity1 testEntity3b = new TestEntity1("uuid3", "content3b");
        final TestEntity1 testEntity4 = new TestEntity1("uuid4", "content4");

        try (final PackageWriterWrapper newPackage = PackageManager.createNewPackage(file.toPath())) {

            newPackage.storeEntity("uuid1", testEntity1);
            newPackage.storeEntity("uuid2", testEntity2);
            newPackage.storeEntity("uuid3", testEntity3b);
            newPackage.storeEntity("uuid4", testEntity4);
        }


        final Optional<PackageReaderWrapper> latestDeltaPackage = PackageManager
                .getLatestDeltaPackage(file.toPath());

        assertTrue(latestDeltaPackage.isPresent());

        final long countedEntities = latestDeltaPackage.get()
                .readEntities(TestEntity1.class)
                .count();

        final long countedTimestamps = latestDeltaPackage.get()
                .readUuidsOfTimestampedEntities(TestEntity1.class)
                .count();

        final long summedAmountOfEntities = latestDeltaPackage.get()
                .getPackageMetadata()
                .mapToLong(meta -> meta.getNumberOfEntities())
                .sum();

        assertEquals(summedAmountOfEntities, countedEntities + countedTimestamps);
    }

    @Test
    public void shouldWriteAndReadLongContent() throws IOException {
        final File tempDir = temporaryFolder.newFolder();

        final TestEntity1 testEntity1 = new TestEntity1("uuid1", multiplyLenght("content1", 1000));
        final TestEntity1 testEntity2 = new TestEntity1("uuid2", multiplyLenght("content2", 1000));
        final TestEntity1 testEntity3 = new TestEntity1("uuid3", multiplyLenght("content3", 1000));

        try (final PackageWriterWrapper newPackage = PackageManager.createNewPackage(tempDir.toPath())) {

            newPackage.storeEntity("uuid1", testEntity1);
            newPackage.storeEntity("uuid2", testEntity2);
            newPackage.storeEntity("uuid3", testEntity3);
        }

        final Optional<PackageReaderWrapper> optionalLatestDeltaPackage = PackageManager.getLatestDeltaPackage(tempDir.toPath());
        assertTrue(optionalLatestDeltaPackage.isPresent());

        final PackageReaderWrapper latestDeltaPackage = optionalLatestDeltaPackage.get();

        final long countFromParsing = latestDeltaPackage.readEntities(TestEntity1.class)
                .count();

        final long summedAmountOfEntities = latestDeltaPackage
                .getPackageMetadata()
                .mapToLong(PackedTableMetadata::getNumberOfEntities)
                .sum();

        assertEquals(countFromParsing, summedAmountOfEntities);
    }

    private String multiplyLenght(final String content, final int times) {
        return LongStream.range(1, times)
                .mapToObj(c -> content)
                .reduce((a, b) -> a + b)
                .get();
    }
}
