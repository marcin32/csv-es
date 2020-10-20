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
    }

    @Test
    public void shouldWriteEntitysTimestampsAndArchive() throws IOException {
        final File tempDir = temporaryFolder.newFolder();
        final PackageWriter packageWriter = new PackageWriter();
        final long timestamp = System.currentTimeMillis();
        final PackageDescriptorForWriting packageDescriptor = new PackageDescriptorForWriting(timestamp, PackageScope.FULL_PACKAGE,
                tempDir.toPath());

        final TestEntity1 testEntity1 = new TestEntity1("a", "a_content");
        final TestEntity1 testEntity2 = new TestEntity1("b", "b_content");
        final TestEntity1 testEntity3 = new TestEntity1("c", "c_content");
        final TestEntity1 testEntity4 = new TestEntity1("d", "d_content");

        packageWriter.storeEntityTimestamp("a", testEntity1, packageDescriptor);
        packageWriter.storeEntityTimestamp("b", testEntity2, packageDescriptor);
        packageWriter.storeEntityTimestamp("c", testEntity3, packageDescriptor);
        packageWriter.storeEntity("d", testEntity4, packageDescriptor);


        packageWriter.saveMetadata(packageDescriptor);
        packageWriter.closePackage(packageDescriptor);

        final PackageDescriptor packageForReading = new PackageDescriptor(timestamp, PackageScope.FULL_PACKAGE,
                PackageType.ARCHIVE, tempDir.toPath());

        final Path basePathWithPackageName = packageForReading.getBasePathWithPackageName();
        assertTrue(basePathWithPackageName.toFile().exists());

        final PackageReader packageReader = new PackageReader();
        final long numberOfEntitiesInMetadata = packageReader.getPackageMetadata(packageForReading)
                .filter(meta -> meta.getClassName().equals(TestEntity1.class.getSimpleName() + Constants.TIMESTAMPS_SUFFIX + Constants.TABLE_EXTENSION))
                .mapToLong(PackedTableMetadata::getNumberOfEntities)
                .findFirst()
                .getAsLong();
        final long countedEntities = packageReader.readUuidsOfTimestampedEntities(TestEntity1.class, packageForReading)
                .count();

        assertEquals(countedEntities, numberOfEntitiesInMetadata);
    }
}
