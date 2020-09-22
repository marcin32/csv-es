package com.badscience.source.reading;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;

public class TableReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldParseEntity() throws URISyntaxException, IOException {
        /*final String fileName = "/testDatabase1.csv";
        final TableReader entityReader = new TableReader();
        final URL resource = this.getClass().getResource(fileName);
        final File file = new File(resource.toURI());

        long entityCount = 0;

        try (final Stream<AbstractSourceEntity<TestEntity1>> abstractSourceEntityStream = entityReader.readEntities(file, TestEntity1.class)) {

            entityCount = abstractSourceEntityStream
                    .count();
        }

        assertEquals("Should read 1 entity", 1, entityCount);*/
    }
}
