package com.marcin32.source.dal.lowlevel.csv;

import com.marcin32.source.dal.lowlevel.file.FileReader;
import com.marcin32.source.model.csv.ITableFormatAdapter;
import com.marcin32.source.model.csv.UnchangedEntityAdapter;
import com.marcin32.source.model.file.RawFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CsvWriterTest {

    private final static ITableFormatAdapter<String> adapter = new UnchangedEntityAdapter();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldWriteACsvFile() throws IOException {
        final String content = "testContentWriter";
        final String fileName = "testFile.csv";
        final File temporaryFolder = folder.newFolder();

        final CsvWriter csvWriter = new CsvWriter();
        final RawFile rawFile = new RawFile(fileName, temporaryFolder.toPath());

        csvWriter.saveEntity(rawFile, adapter, content);
        csvWriter.closeCsvFile(rawFile);
        assertEquals(1, rawFile.getNumberOfLines());

        final FileReader fileReader = new FileReader();
        final RawFile rawFileToRead = new RawFile(fileName, temporaryFolder.toPath());
        try (final Stream<String> stringStream = fileReader.readFile(rawFileToRead)) {

            final List<String> stringList = stringStream.collect(Collectors.toList());
            final Optional<String> first = stringList.stream().findFirst();
            final int linesCount = stringList.size();

            assertTrue(first.isPresent());
            assertEquals(content, first.get());
            assertEquals(1, linesCount);
        }
    }
}
