package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.model.CsvEntry;
import com.marcin32.source.model.SourceEntry;
import com.marcin32.source.model.file.RawFile;
import com.marcin32.source.utils.FilesystemDal;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TableReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadTestFile() throws IOException {

        final String fileName = "testDatabase1.csv";
        final TableReader tableReader = new TableReader();
        final File file = FilesystemDal.getFileFromResources(fileName);
        final RawFile rawFile = new RawFile(fileName, file.toPath().getParent());

        long sum = 0;
        try (final Stream<SourceEntry<CsvEntry>> stringStream = tableReader.readEntities(rawFile, CsvEntry.class)) {
            sum = stringStream
                    .mapToLong(CsvEntry::getTimestamp)
                    .sum();
        }

        assertEquals("Should read 6 lines", 123456, sum);
    }
}
