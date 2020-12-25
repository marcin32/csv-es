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
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
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
}
