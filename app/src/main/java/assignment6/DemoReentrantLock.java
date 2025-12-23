package assignment6;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
public class DemoReentrantLock {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    // Only called by Thread-1
    public void increment() throws InterruptedException {
        Random rand = new Random();
        try {
            Thread.sleep(rand.nextInt(7));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        }
        lock.lock(); // Acquire the lock
        try {
            // Critical section: only one thread can execute this at a time
            count++;
            System.out.println(Thread.currentThread().getName() + " incremented count to: " + count);
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    // Only called by Thread-2
    public void performNestedOperation() throws InterruptedException {
        Random rand = new Random();
        try {
            Thread.sleep(rand.nextInt(7));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } // Simulate some work
        lock.lock(); // Acquire the lock (reentrant behavior)
        try {
            System.out.println(Thread.currentThread().getName() + " entered nested operation.");
            increment(); // This thread already holds the lock, so it can re-acquire it.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    public static void showReentrantLock(int numLoops) {
    DemoReentrantLock example = new DemoReentrantLock();

    int numTasks = 7;
    Thread[] threads = new Thread[numTasks];

    for (int i = 0; i < numTasks; i++) {
        final int taskIndex = i;

        Runnable task = () -> {
            for (int loop = 0; loop < numLoops; loop++) {
                try {
                    if (taskIndex % 2 == 0) {
                        example.increment();
                    } else {
                        example.performNestedOperation();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        threads[i] = new Thread(task, "Thread-" + (i + 1));
    }

    // Start threads
    for (Thread t : threads) {
        t.start();
    }

    // Wait for threads to finish
    for (Thread t : threads) {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            break;
        }
    }

    System.out.println("showReentrantLock complete.");
}

}
