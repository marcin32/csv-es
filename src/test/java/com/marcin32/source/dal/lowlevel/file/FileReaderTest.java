package com.marcin32.source.dal.lowlevel.file;

import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;
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
        final String fileName = "testFile1.txt";
        final FileReader fileReader = new FileReader();
        final File resource = getFile(fileName);
        final RawFile rawFile = new RawFile(fileName, resource.toPath().getParent());

        long lineCount = 0;
        try (final Stream<String> stringStream = fileReader.readFile(rawFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 6 lines", 6, lineCount);
    }

    //@Ignore
    @Test
    public void shouldReadPackedFileLineAfterLine() throws URISyntaxException {
        final String fileName = "testFile1.txt";
        String archiveName = "updates1/2345-DELTA.tar.gz";
        final FileReader fileReader = new FileReader();
        final File file = getFile(archiveName);
        final PackedFile rawFile = new PackedFile(fileName, file.toPath());

        long lineCount = 0;
        try (final Stream<String> stringStream = fileReader.readFile(rawFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 6 lines", 6, lineCount);
    }

    private File getFile(final String archiveName) {
        final URL url = ClassLoader.getSystemResource(archiveName);
        return new File(url.getFile());
    }
}
