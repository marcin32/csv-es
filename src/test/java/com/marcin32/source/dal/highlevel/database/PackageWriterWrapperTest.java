package com.marcin32.source.dal.highlevel.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackageDescriptorForWriting;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.utils.FilesystemDal;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PackageWriterWrapperTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldWriteEntityTimestampsAndArchive() throws Exception {

        final String specialDicritics = "ążćłó";
        final File tempDir = temporaryFolder.newFolder();
        final long timestamp = System.currentTimeMillis();
        final PackageDescriptorForWriting packageDescriptor = new PackageDescriptorForWriting(timestamp,
                PackageScope.FULL_PACKAGE,
                tempDir.toPath());
        try (final PackageWriterWrapper packageWriterWrapper = new PackageWriterWrapper(packageDescriptor)) {

            final TestEntity1 testEntity1 = new TestEntity1("a", "a_content");
            final TestEntity1 testEntity2 = new TestEntity1("b", "b_content");
            final TestEntity1 testEntity3 = new TestEntity1("c", "c_content");
            final TestEntity1 testEntity4 = new TestEntity1("d", specialDicritics);

            packageWriterWrapper.storeEntity("a", testEntity1);
            packageWriterWrapper.storeEntity("b", testEntity2);
            packageWriterWrapper.storeEntity("c", testEntity3);
            packageWriterWrapper.storeEntity("d", testEntity4);

        }

        final PackageDescriptor packageForReading = new PackageDescriptor(timestamp, PackageScope.FULL_PACKAGE,
                PackageType.ARCHIVE, tempDir.toPath());

        final Path basePathWithPackageName = packageForReading.getBasePathWithPackageName();
        assertTrue(basePathWithPackageName.toFile().exists());

        final PackageReaderWrapper packageReaderWrapper = new PackageReaderWrapper(packageForReading);
        final long numberOfEntitiesInMetadata = packageReaderWrapper
                .getPackageMetadata()
                .filter(meta -> meta.getFileName().equals(TestEntity1.class.getSimpleName() + Constants.TABLE_EXTENSION))
                .mapToLong(PackedTableMetadata::getNumberOfEntities)
                .findFirst()
                .getAsLong();
        final long countedEntities = packageReaderWrapper
                .readEntities(TestEntity1.class)
                .count();

        assertEquals(countedEntities, numberOfEntitiesInMetadata);

        final Optional<SourceEntry<TestEntity1>> dEntity = packageReaderWrapper
            .readEntities(TestEntity1.class)
            .filter(e -> e.getUuid().equals("d"))
            .findFirst();
        assertTrue(dEntity.isPresent());
        assertEquals(specialDicritics, dEntity.get().getEntity().getContent());
    }


    @Test
    public void shouldCreateValidDeltaWhenPreviousPackageIsEmpty() throws IOException {

        // Copy package to now temp dir
        final Path workingDirPath = temporaryFolder.newFolder().toPath();
        final Path workingDirPackagePath = workingDirPath.resolve("1234-FULL_PACKAGE.tar.gz");
        final Path emptyPackagePath = new File(ClassLoader.getSystemResource("empty_package/1234-FULL_PACKAGE.tar.gz").getPath()).toPath();
        FilesystemDal.copyFile(emptyPackagePath, workingDirPackagePath);

        // create a new package
        final long timestamp = System.currentTimeMillis();
        final PackageDescriptorForWriting packageDescriptor = new PackageDescriptorForWriting(2345L,
            PackageScope.FULL_PACKAGE,
            workingDirPath);
        try (final PackageWriterWrapper packageWriterWrapper = new PackageWriterWrapper(packageDescriptor)) {

            final TestEntity1 testEntity1 = new TestEntity1("a", "a_content");
            final TestEntity1 testEntity2 = new TestEntity1("b", "b_content");
            final TestEntity1 testEntity3 = new TestEntity1("c", "c_content");
            final TestEntity1 testEntity4 = new TestEntity1("d", "d_content");

            packageWriterWrapper.storeEntity("a", testEntity1);
            packageWriterWrapper.storeEntity("b", testEntity2);
            packageWriterWrapper.storeEntity("c", testEntity3);
            packageWriterWrapper.storeEntity("d", testEntity4);

        }

        // validate new delta package has all records
        final Optional<PackageReaderWrapper> latestDeltaPackage = PackageManager.getLatestDeltaPackage(workingDirPath);
        assertTrue(latestDeltaPackage.isPresent());
        final long countOfEntries = latestDeltaPackage.get().readEntities(TestEntity1.class).count();
        assertEquals(4, countOfEntries);

        final PackageDescriptorForWriting packageDescriptor2 = new PackageDescriptorForWriting(3456L,
            PackageScope.FULL_PACKAGE,
            workingDirPath);
        try (final PackageWriterWrapper packageWriterWrapper = new PackageWriterWrapper(packageDescriptor2)) {

            final TestEntity1 testEntity1 = new TestEntity1("a", "a_content");
            final TestEntity1 testEntity2 = new TestEntity1("b", "b_content");
            final TestEntity1 testEntity3 = new TestEntity1("c", "c_content_new");
            final TestEntity1 testEntity4 = new TestEntity1("d", "d_content_new");

            packageWriterWrapper.storeEntity("a", testEntity1);
            packageWriterWrapper.storeEntity("b", testEntity2);
            packageWriterWrapper.storeEntity("c", testEntity3);
            packageWriterWrapper.storeEntity("d", testEntity4);
        }

        // validate next delta package has changed and timestamped records
        final Optional<PackageReaderWrapper> latestDeltaPackage2 = PackageManager.getLatestDeltaPackage(workingDirPath);
        assertTrue(latestDeltaPackage2.isPresent());
        final long countOfEntries2 = latestDeltaPackage2.get().readEntities(TestEntity1.class).count();
        assertEquals(2, countOfEntries2);

        final Set<String> timestampedUuids = latestDeltaPackage2.get()
            .readUuidsOfTimestampedEntities(TestEntity1.class)
            .collect(Collectors.toSet());
        assertThat(timestampedUuids, CoreMatchers.hasItems("a", "b"));

        final Set<String> changedEntities = latestDeltaPackage2.get()
            .readEntities(TestEntity1.class)
            .map(SourceEntry::getEntity)
            .map(TestEntity1::getContent)
            .collect(Collectors.toSet());
        assertThat(changedEntities, CoreMatchers.hasItems("c_content_new", "d_content_new"));
    }
}
