package assignment6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DemoReadWriteLock {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private int sharedResource = 0;

    public int readResource() {
        readLock.lock(); // Acquire the read lock
        try {
            // Perform read operations on the shared resource
            System.out.println(Thread.currentThread().getName() + " is reading: " + sharedResource);
            return sharedResource;
        } finally {
            readLock.unlock(); // Release the read lock
        }
    }

    public void writeResource(int value) {
        writeLock.lock(); // Acquire the write lock
        try {
            // Perform write operations on the shared resource
            sharedResource = value;
            System.out.println(Thread.currentThread().getName() + " is writing: " + sharedResource);
        } finally {
            writeLock.unlock(); // Release the write lock
        }
    }

    public static void showReadWriteLock(String[] args) {
    DemoReadWriteLock example = new DemoReadWriteLock();

    int numReaders = 20;
    int numWriters = 7;

    // Create reader threads
    Runnable readers = () -> {
        for (int i = 0; i < numReaders; i++) {
            final int readerIndex = i;
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    example.readResource();
                    try {
                        Thread.sleep(7); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "Reader-" + readerIndex).start();
        }
    };

    // Create writer threads
    Runnable writers = () -> {
        for (int i = 0; i < numWriters; i++) {
            final int writerIndex = i;
            new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    example.writeResource(writerIndex * 10 + j);
                    try {
                        Thread.sleep(3); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "Writer-" + writerIndex).start();
        }
    };

    // Start readers and writers
    readers.run();
    writers.run();
}

}
