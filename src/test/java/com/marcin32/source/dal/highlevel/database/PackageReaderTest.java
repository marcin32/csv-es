package com.marcin32.source.dal.highlevel.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.base.Constants;
import com.marcin32.source.model.ITableMetadata;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.Test;

public class PackageReaderTest {

    @Test
    public void shouldOpenPackageAndAccessMetadata() {
        final PackageDescriptor package2345 = CommonTestResources.get2345Package();
        final PackageReader packageReader = new PackageReader();

        try (final Stream<PackedTableMetadata> tableMetadata = packageReader.getPackageMetadata(package2345)) {
            final Optional<PackedTableMetadata> metadata = tableMetadata.findAny();
            assertTrue(metadata.isPresent());
        }
    }

    @Test
    public void shouldReadEntitiesBasedOnMetadata() {
        final PackageDescriptor package2345 = CommonTestResources.get2345Package();
        final PackageReader packageReader = new PackageReader();

        try (final Stream<PackedTableMetadata> tableMetadataStream = packageReader.getPackageMetadata(package2345)) {
            final Optional<PackedTableMetadata> metadata = tableMetadataStream.findAny();
            assertTrue(metadata.isPresent());

            final PackedTableMetadata packedTableMetadata1 = metadata.get();

            assertEquals(packedTableMetadata1.getFileName(), TestEntity1.class.getSimpleName() + Constants.TABLE_EXTENSION);
            try (final Stream<SourceEntry<TestEntity1>> sourceEntryStream = packageReader.readEntities(TestEntity1.class, package2345)) {
                long numberOfEntities = sourceEntryStream.count();
                assertEquals(numberOfEntities, packedTableMetadata1.getNumberOfEntities());
            }
        }
    }

    @Test
    public void shouldReadEntitiesBasedOnMetadataFromDirectory() {
        final PackageDescriptor package1234 = CommonTestResources.get1234Package();
        final PackageReader packageReader = new PackageReader();

        try (final Stream<PackedTableMetadata> tableMetadataStream = packageReader.getPackageMetadata(package1234)) {
            final Optional<PackedTableMetadata> metadata = tableMetadataStream.findAny();
            assertTrue(metadata.isPresent());

            final ITableMetadata packedTableMetadata1 = metadata.get();

            assertEquals(packedTableMetadata1.getFileName(), TestEntity1.class.getSimpleName() + Constants.TABLE_EXTENSION);
            try (final Stream<SourceEntry<TestEntity1>> sourceEntryStream = packageReader.readEntities(TestEntity1.class, package1234)) {
                long numberOfEntities = sourceEntryStream.count();
                assertEquals(numberOfEntities, packedTableMetadata1.getNumberOfEntities());
            }
        }
    }

    @Test
    public void shouldContainFile() {
        final PackageDescriptor package2345 = CommonTestResources.get2345Package();
        final PackageReader packageReader = new PackageReader();

        final String entityFileName = packageReader.getEntityFileName(TestEntity1.class);
        final boolean shouldContainFile = packageReader.doesContainFile(entityFileName, package2345);
        assertTrue(shouldContainFile);

        final String timestampedFileName = packageReader.getTimestampedFileName(TestEntity1.class);
        final boolean shouldNotContainFile = packageReader.doesContainFile(timestampedFileName, package2345);
        assertFalse(shouldNotContainFile);
    }
}
