package com.marcin32.source.dal.highlevel.database;

import com.marcin32.source.TestEntity1;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackageManagerThreadripperTest {

    public static final int ENTITY_COUNT = 1000;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldCreateDeltaPackage() throws IOException, InterruptedException {
        final File file = temporaryFolder.newFolder();
        final Random random = new Random();

        final List<Thread> threads = new ArrayList<>(64);

        try (final PackageWriterWrapper newPackage = PackageManager.createNewPackage(file.toPath())) {

            for (int i = 0; i < 64; ++i) {
                final Runnable runnable = thread(i, newPackage, random);
                final Thread thread = new Thread(runnable);
                threads.add(thread);
            }

            threads.forEach(Thread::start);

            for (final Thread th : threads) {
                th.join();
            }
        }

        final Optional<PackageReaderWrapper> latestDeltaPackage = PackageManager
                .getLatestDeltaPackage(file.toPath());

        assertTrue(latestDeltaPackage.isPresent());

        final long count = PackageManager
                .streamFullPackages(file.toPath())
                .map(PackageManager::getPackageReader)
                .flatMap(readerWrapper -> readerWrapper.readEntities(TestEntity1.class))
                .count();

        assertEquals(count, 64 * ENTITY_COUNT);
    }

    @Test
    public void shouldCreateSecondDeltaPackage() throws IOException, InterruptedException {
        final File file = temporaryFolder.newFolder();
        final Random random = new Random();

        List<Thread> threads = new ArrayList<>(64);

        try (final PackageWriterWrapper newPackage = PackageManager.createNewPackage(file.toPath())) {

            for (int i = 0; i < 64; ++i) {
                final Runnable runnable = thread(i, newPackage, random);
                final Thread thread = new Thread(runnable);
                threads.add(thread);
            }

            threads.forEach(Thread::start);

            for (final Thread th : threads) {
                th.join();
            }
        }

        Optional<PackageReaderWrapper> latestDeltaPackage = PackageManager
                .getLatestDeltaPackage(file.toPath());

        assertTrue(latestDeltaPackage.isPresent());

        threads = new ArrayList<>(64);

        try (final PackageWriterWrapper newPackage = PackageManager.createNewPackage(file.toPath())) {

            for (int i = 0; i < 64; ++i) {
                final Runnable runnable = thread(i, newPackage, random);
                final Thread thread = new Thread(runnable);
                threads.add(thread);
            }

            threads.forEach(Thread::start);

            for (final Thread th : threads) {
                th.join();
            }
        }

        latestDeltaPackage = PackageManager
                .getLatestDeltaPackage(file.toPath());

        assertTrue(latestDeltaPackage.isPresent());
    }

    private Runnable thread(final int i, final PackageWriterWrapper newPackage, final Random random) {
        return () -> {
            for (int j = 0; j < ENTITY_COUNT; ++j) {
                final int index = random.nextInt();
                final String uuid = "uuid" + index;
                final TestEntity1 testEntity1 = new TestEntity1(uuid, multiplyLenght("content1" + i + index, 11));
                newPackage.storeEntity(uuid, testEntity1);
            }
        };
    }

    private String multiplyLenght(final String content, final int times) {
        return LongStream.range(1, times)
                .mapToObj(c -> content)
                .reduce((a, b) -> a + b)
                .get();
    }
}
