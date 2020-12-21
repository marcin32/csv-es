package com.marcin32.source.dal.highlevel.database;

import static org.junit.Assert.assertEquals;

import com.marcin32.source.TestEntity1;
import com.marcin32.source.model.PackageDescriptor;
import com.marcin32.source.model.SourceEntry;
import java.util.stream.Stream;
import org.junit.Test;

public class PackageReaderWrapperTest {

  @Test
  public void shouldNotReadUuidsOfTimestampedEntitiesNotExistingClass() {

    final PackageDescriptor package2345 = CommonTestResources.get2345Package();
    final PackageReader packageReader = new PackageReader();

    try (final Stream<String> sourceEntryStream = packageReader.readUuidsOfTimestampedEntities(TestEntity1.class, package2345)) {
      long numberOfEntities = sourceEntryStream.count();
      assertEquals(numberOfEntities, 0);
    }
  }

  @Test
  public void shouldNotReadEntitiesOfNotExistingClass() {

    final PackageDescriptor package2345 = CommonTestResources.get2345Package();
    final PackageReader packageReader = new PackageReader();

    try (final Stream<SourceEntry<TestEntity2>> sourceEntryStream = packageReader.readEntities(TestEntity2.class, package2345)) {
      long numberOfEntities = sourceEntryStream.count();
      assertEquals(numberOfEntities, 0);
    }
  }

  class TestEntity2 {

  }
}