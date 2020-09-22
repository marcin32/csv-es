package com.marcin32.source.reading;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;

public class ReadingUpdateDescriptorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadFromFile() throws IOException, URISyntaxException {
        /*final URI basePath = this.getClass().getResource("/updates1").toURI();
        final Path filePath = Paths.get(Paths.get(basePath).toString(), "1234-FULL_UPDATE");
        final ReadingPackageDescriptor abstractUpdateDescriptor = new ReadingPackageDescriptor(1234L, filePath,
                UpdateScope.FULL_UPDATE, UpdateType.DIRECTORY);

        long entityCount = 0;
        try(final Stream<AbstractCsvEntity> abstractSourceEntityStream
                = abstractUpdateDescriptor.readEntities( "testDatabase1")) {
            entityCount = abstractSourceEntityStream.count();
        }

        assertEquals("Should read entity", 1, entityCount);*/
    }

    @Test
    public void shouldReadFromArchive() throws IOException, URISyntaxException {
        /*final URI basePath = this.getClass().getResource("/updates1").toURI();
        final Path filePath = Paths.get(Paths.get(basePath).toString(), "2345-DELTA.tar.gz");
        final ReadingPackageDescriptor abstractUpdateDescriptor = new ReadingPackageDescriptor(2345L, filePath,
                UpdateScope.DELTA, UpdateType.ARCHIVE);

        long entityCount = 0;
        try(final Stream<AbstractCsvEntity> abstractSourceEntityStream
                    = abstractUpdateDescriptor.readEntities("testDatabase1")) {
            entityCount = abstractSourceEntityStream.count();
        }

        assertEquals("Should read entity", 1, entityCount);*/
    }

    @Test
    public void shouldReadTimestampsTable() throws IOException, URISyntaxException {
        /*final URI basePath = this.getClass().getResource("/updates1").toURI();
        final Path filePath = Paths.get(Paths.get(basePath).toString(), "1234-FULL_UPDATE");
        final ReadingPackageDescriptor abstractUpdateDescriptor = new ReadingPackageDescriptor(1234L, filePath,
                UpdateScope.FULL_UPDATE, UpdateType.DIRECTORY);*/
    }
}
