package com.marcin32.source.dal.highlevel.table;

import com.marcin32.source.TestEntity1;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class AbstractTableReaderTest {

    @Test
    public void shouldGenerateDifferentHashesForDifferentContent() {
        final TestEntity1 testEntity1 = new TestEntity1("uuid1", "content");
        final TestEntity1 testEntity2 = new TestEntity1("uuid2", "content");
        final AbstractTableReader tableReader = new TableReader();

        final String entityHash1 = tableReader.getEntityHash(testEntity1);
        final String entityHash2 = tableReader.getEntityHash(testEntity2);

        assertNotEquals(entityHash1, entityHash2);
    }

    @Test
    public void shouldFindEntity() {
        final AbstractTableReader tableReader = new TableReader();
        //checkWhetherTableContainsEntity
    }

}