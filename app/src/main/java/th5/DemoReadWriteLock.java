package th5;
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

        Runnable readers = () -> {
            for (int i = 0; i < 5; i++) {
                new Thread(() -> {
                    for (int j = 0; j < 3; j++) {
                        example.readResource();
                        try {
                            Thread.sleep(7); // Simulate some work
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }, "Reader-" + i).start();
            }
        };

        // Create multiple writer threads
        Runnable writers = () -> {
            for (int i2 = 0; i2 < 2; i2++) {
                final int writerIndex = i2;
                new Thread(() -> {
                    for (int j = 0; j < 2; j++) {
                        example.writeResource(writerIndex * 10 + j);
                        try {
                            Thread.sleep(3); // Simulate some work
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }, "Writer-" + writerIndex).start();
            }
        };
        readers.run();
        writers.run();
       
    }
}
