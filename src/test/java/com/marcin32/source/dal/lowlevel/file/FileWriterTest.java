package com.marcin32.source.dal.lowlevel.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public class FileWriterTest {

    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldWriteALine() throws IOException {
        /*final String content = "testContentWriter";
        final String databaseName = "testDatabaseName.txt";
        final File temporaryFolder = folder.newFolder();

        final FileWriter fileWriter = new FileWriter();
        final FileDescriptors fileDescriptors = new FileDescriptors();
        final WritingPackageDescriptor writingUpdateDescriptor = new WritingPackageDescriptor(1234L,
                temporaryFolder.toPath(), UpdateScope.FULL_UPDATE, UpdateType.DIRECTORY);

        fileWriter.appendFile(content, databaseName, writingUpdateDescriptor, fileDescriptors);
        fileDescriptors.closeAllFiles();

        final FileReader fileReader = new FileReader();
        final Path path = Paths.get(temporaryFolder.toString(),
                //updateDatabaseInfo.getTimestamp() + "-" + updateDatabaseInfo.getUpdateScope().toString(),
                databaseName);
        try(final Stream<String> stringStream = fileReader.readFile(path)) {

            final List<String> stringList = stringStream.collect(Collectors.toList());
            final Optional<String> first = stringList.stream().findFirst();
            final int linesCount = stringList.size();

            assertTrue(first.isPresent());
            assertEquals(content, first.get());
            assertEquals(1, linesCount);
        }*/
    }
}
