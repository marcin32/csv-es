package com.marcin32.source.dal.lowlevel.file;

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

public class FileWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void shouldWriteALine() throws IOException {
        final String content = "testContentWriter";
        final String fileName = "testFile.txt";
        final File temporaryFolder = folder.newFolder();

        final FileWriter fileWriter = new FileWriter();
        final RawFile rawFile = new RawFile(fileName, temporaryFolder.toPath());

        fileWriter.appendFile(content, rawFile);
        fileWriter.closeFile(rawFile);
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
