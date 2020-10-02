package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.model.file.PackedFile;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CsvReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadTestFile() throws IOException {

        final String fileName = "testDatabase1.csv";
        final CsvReader csvReader = new CsvReader();
        final File file = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(fileName, file.toPath().getParent());

        long sum = 0;
        try (final Stream<String[]> stringStream = csvReader.readCsv(rawFile)) {
            sum = stringStream
                    .mapToLong(csvLine -> Long.parseLong(csvLine[1]))
                    .sum();
        }

        assertEquals("Should read 1 csv entry", 123456, sum);
    }

    @Test
    public void shouldReadPackedFileLineAfterLine() throws IOException {
        final String fileName = "TestEntity1.csv";
        String archiveName = "updates1/2345-DELTA_PACKAGE.tar.gz";
        final CsvReader csvReader = new CsvReader();
        final File file = FilesystemDal.getFileFromResources(archiveName);
        final PackedFile packedFile = new PackedFile(fileName, file.toPath());

        long lineCount = 0;
        try (final Stream<String[]> stringStream = csvReader.readCsv(packedFile)) {
            lineCount = stringStream.count();
        }

        assertEquals("Should read 1 lines", 1, lineCount);
    }
}
