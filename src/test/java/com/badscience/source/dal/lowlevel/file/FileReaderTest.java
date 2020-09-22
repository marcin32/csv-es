package com.badscience.source.dal.lowlevel.file;

import com.badscience.source.model.file.PackedFile;
import com.badscience.source.model.file.RawFile;
import com.badscience.source.utils.ClassLoaderUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class FileReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadRawFileLineAfterLine() throws URISyntaxException {
        final String fileName = "/testFile1.txt";
        final FileReader fileReader = new FileReader();
        final URL resource = this.getClass().getResource(fileName);
        final File file = new File(resource.toURI());
        final RawFile rawFile = new RawFile(fileName, file.toPath().getParent());

        long lineCount = 0;
        try (final Stream<String> stringStream = fileReader.readFile(rawFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 6 lines", 6, lineCount);
    }

    @Test
    public void shouldReadPackedFileLineAfterLine() throws URISyntaxException {
        final String fileName = "testFile1.txt";
        String archiveName = "/updates1/2345-DELTA.tar.gz";
        final FileReader fileReader = new FileReader();
        final URL archive = this.getClass().getResource(archiveName);
        final URL resource = ClassLoaderUtil.getResource(archiveName, FileReaderTest.class);
        System.out.println(resource);
        System.out.println(archive);
        final File file = new File(getFirstNotNull(resource, archive).toURI());
        final PackedFile rawFile = new PackedFile(fileName, file.toPath());

        long lineCount = 0;
        try (final Stream<String> stringStream = fileReader.readFile(rawFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 6 lines", 6, lineCount);
    }

    private URL getFirstNotNull(final URL resource, final URL archive) {
        return resource == null ? archive : resource;
    }
}
