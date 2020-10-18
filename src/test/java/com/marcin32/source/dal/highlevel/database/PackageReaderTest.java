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
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class PackageReaderTest {

    @Test
    public void shouldOpenPackageAndAccessMetadata() {
        final PackageDescriptor package2345 = get2345Package();
        final PackageReader packageReader = new PackageReader(package2345);

        try (final Stream<PackedTableMetadata> tableMetadata = packageReader.listTables()) {
            final Optional<PackedTableMetadata> any = tableMetadata.findAny();
            assertTrue(any.isPresent());
        } catch (final Throwable th) {
            fail();
        }
    }

    @Test
    public void shouldReadEntitiesBasedOnMetadata() throws IOException {
        final PackageDescriptor package2345 = get2345Package();
        final PackageReader packageReader = new PackageReader(package2345);

        try (final Stream<PackedTableMetadata> tableMetadataStream = packageReader.listTables()) {
            final Optional<PackedTableMetadata> any = tableMetadataStream.findAny();
            assertTrue(any.isPresent());

            final PackedTableMetadata packedTableMetadata1 = any.get();

            assertEquals(packedTableMetadata1.getClassName(), TestEntity1.class.getSimpleName() + Constants.TABLE_EXTENSION);
            try (final Stream<SourceEntry<TestEntity1>> sourceEntryStream = packageReader.readEntities(TestEntity1.class)) {
                long numberOfEntities = sourceEntryStream.count();
                assertEquals(numberOfEntities, packedTableMetadata1.getNumberOfEntities());
            }
        }
    }

    @Test
    public void shouldReadEntitiesBasedOnMetadataFromDirectory() throws IOException {
        final PackageDescriptor package1234 = get1234Package();
        final PackageReader packageReader = new PackageReader(package1234);

        try (final Stream<PackedTableMetadata> tableMetadataStream = packageReader.listTables()) {
            final Optional<PackedTableMetadata> any = tableMetadataStream.findAny();
            assertTrue(any.isPresent());

            final ITableMetadata packedTableMetadata1 = any.get();

            assertEquals(packedTableMetadata1.getClassName(), TestEntity1.class.getSimpleName() + Constants.TABLE_EXTENSION);
            try (final Stream<SourceEntry<TestEntity1>> sourceEntryStream = packageReader.readEntities(TestEntity1.class)) {
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
