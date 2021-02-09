package com.marcin32.source.model.file;

import java.io.IOException;
import java.util.stream.Stream;

public class EmptyFile extends AbstractFile {

  public EmptyFile(final String fileName) {
    super(fileName);
  }

  public EmptyFile() {
    super("");
  }

  @Override
  public Stream<String> readFile() throws IllegalAccessException, IOException {
    return Stream.empty();
  }

  @Override
  public long getNumberOfEntries() {
    return 0;
  }
}
