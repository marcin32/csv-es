package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.file.RawFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CsvReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadTestFile() throws IOException, URISyntaxException {

        final String fileName = "/testDatabase1.csv";
        final CsvReader csvReader = new CsvReader();
        final URL resource = this.getClass().getResource(fileName);
        final File file = new File(resource.toURI());
        final RawFile rawFile = new RawFile(fileName, file.toPath().getParent());

        long sum = 0;
        try (final Stream<CsvEntry> stringStream = csvReader.readCsv(rawFile)) {
            sum = stringStream
                    .mapToLong(CsvEntry::getTimestamp)
                    .sum();
        }

        assertEquals("Should read 6 lines", 123456, sum);
    }
}
