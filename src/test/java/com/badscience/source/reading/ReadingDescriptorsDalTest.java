package com.badscience.source.reading;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class ReadingDescriptorsDalTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {

        temporaryFolder.newFolder("backup");
        temporaryFolder.newFile("1234-FULL_UPDATE.tar.gz");
        temporaryFolder.newFile("2345-FULL_UPDATE.tar.gz");
        temporaryFolder.newFile("3456-FULL_UPDATE.tar.gz");
        temporaryFolder.newFile("4567-FULL_UPDATE.tar.gz");

        temporaryFolder.newFile("1234-DELTA.tar.gz");
        temporaryFolder.newFile("2345-DELTA.tar.gz");
        temporaryFolder.newFile("3456-DELTA.tar.gz");
        temporaryFolder.newFile("4567-DELTA.tar.gz");
    }

    @Test
    public void shouldReturnLatestDeltaPackage() {
        /*final ReadingDescriptorsDal readingDescriptorsDal = new ReadingDescriptorsDal();

        final Optional<ReadingPackageDescriptor> lastDelta = readingDescriptorsDal.getLastDelta(temporaryFolder.getRoot().toPath());

        assertTrue(lastDelta.isPresent());
        assertEquals((long) lastDelta.get().getTimestamp(), 4567);*/
    }

    @Test
    public void shouldReturnLatestFullUpdatePackage() {
        /*final ReadingDescriptorsDal readingDescriptorsDal = new ReadingDescriptorsDal();

        final Optional<ReadingPackageDescriptor> lastFullUpdate = readingDescriptorsDal.getLastFullUpdate(temporaryFolder.getRoot().toPath());

        assertTrue(lastFullUpdate.isPresent());
        assertEquals((long) lastFullUpdate.get().getTimestamp(), 4567);*/
    }

}
