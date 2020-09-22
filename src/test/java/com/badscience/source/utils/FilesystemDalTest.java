package com.badscience.source.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URISyntaxException;

public class FilesystemDalTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFindAllUpdates() throws URISyntaxException {

        /*final FilesystemDal filesystemDal = new FilesystemDal();
        final String fileName = "/updates1";
        final URL resource = this.getClass().getResource(fileName);
        final File file = new File(resource.toURI());

        final Set<ReadingPackageDescriptor> abstractUpdateDescriptors = filesystemDal.listUpdates(file.toPath());
        assertEquals("Number of found repositories", 3, abstractUpdateDescriptors.size());*/
    }
}
