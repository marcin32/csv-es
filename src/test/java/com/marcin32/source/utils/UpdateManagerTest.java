package com.marcin32.source.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;

public class UpdateManagerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldReadLatestEntity() throws URISyntaxException, IOException {
//        final Path basePath = Paths.get(this.getClass().getResource("/updates1").toURI());
//        final String databaseFileName = "testDatabase1.csv";
//        final UpdateManager updateManager = new UpdateManager();
//
//        try (final Stream<TestEntity1> stringStream = updateManager.readEntities(basePath, databaseFileName,
//                TestEntity1.class)) {
//            final Optional<TestEntity1> first = stringStream.findFirst();
//
//            assertTrue(first.isPresent());
//            assertEquals("Should read latest content", "testContent2", first.get().getContent());
//
//        }
    }
}
