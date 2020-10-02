package com.marcin32.source.model.file;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class RawFileTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test(expected = IllegalAccessException.class)
    public void shouldThrowExceptionWhenFileNotClosed() throws IOException, IllegalAccessException {
        final String fileName = "testFile.txt";
        final File temporaryFolder = folder.newFolder();
        final RawFile rawFile = new RawFile(fileName, temporaryFolder.toPath());

        rawFile.appendLine("line");
        rawFile.readFile();
    }

    @Test
    public void shouldReturnProperPath() throws IOException, IllegalAccessException {
        final String fileName = "testFile.txt";
        final Path temporaryPath = folder.newFolder().toPath();
        final RawFile rawFile = new RawFile(fileName, temporaryPath);

        assertEquals(temporaryPath, rawFile.getDirectoryPath());
        assertEquals(temporaryPath.resolve(fileName), rawFile.getDirectoryPathWithFileName());
    }

}
