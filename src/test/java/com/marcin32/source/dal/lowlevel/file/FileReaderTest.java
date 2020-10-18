package com.marcin32.source.dal.lowlevel.file;

import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;
import org.junit.Test;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class FileReaderTest {

    @Test
    public void shouldReadRawFileLineAfterLine() {
        final String fileName = "testFile1.txt";
        final FileReader fileReader = new FileReader();
        final File resource = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(fileName, resource.toPath().getParent());

        long lineCount = 0;
        try (final Stream<String> stringStream = fileReader.readFile(rawFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 6 lines", 6, lineCount);
    }

    @Test
    public void shouldReadPackedFileLineAfterLine() {
        final String fileName = "testFile1.txt";
        String archiveName = "updates1/2345-DELTA_PACKAGE.tar.gz";
        final FileReader fileReader = new FileReader();
        final File file = FilesystemDal.getFileFromResources(archiveName);
        final PackedFile packedFile = new PackedFile(fileName, 6, file.toPath());

        long lineCount = 0;
        try (final Stream<String> stringStream = fileReader.readFile(packedFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 6 lines", 6, lineCount);
    }
}
