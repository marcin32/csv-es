package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.base.Constants;
import com.marcin32.source.base.PackageScope;
import com.marcin32.source.base.PackageType;
import com.marcin32.source.model.ITableMetadata;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.PackedTableMetadata;
import com.marcin32.source.model.SourceEntry;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackageReaderTest {

    @Test
    public void shouldOpenPackageAndAccessMetadata() {
        final PackageDescriptor package2345 = get2345Package();
        final PackageReader packageReader = new PackageReader();

        try (final Stream<PackedTableMetadata> tableMetadata = packageReader.getPackageMetadata(package2345)) {
            final Optional<PackedTableMetadata> metadata = tableMetadata.findAny();
            assertTrue(metadata.isPresent());
        }
    }

    @Test
    public void shouldReadEntitiesBasedOnMetadata() {
        final PackageDescriptor package2345 = get2345Package();
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
        final PackageDescriptor package1234 = get1234Package();
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

    private PackageDescriptor get2345Package() {
        final Path path = new File(ClassLoader.getSystemResource("updates1/").getPath()).toPath();
        return new PackageDescriptor(2345L, PackageScope.DELTA_PACKAGE, PackageType.ARCHIVE, path);
    }

    private PackageDescriptor get1234Package() {
        final Path path = new File(ClassLoader.getSystemResource("updates1/").getPath()).toPath();
        return new PackageDescriptor(1234L, PackageScope.FULL_PACKAGE, PackageType.DIRECTORY, path);
    }
}
