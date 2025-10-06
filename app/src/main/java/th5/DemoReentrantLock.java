package th5;

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
        Runnable task1 = () -> {
            for (int i = 0; i < numLoops; i++) {
                try {
                    example.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        Runnable task2 = () -> {
            for (int i = 0; i < numLoops; i++) {
                try {
                    example.performNestedOperation();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        Thread thread1 = new Thread(task1, "Thread-1");
        Thread thread2 = new Thread(task2, "Thread-2");
        try {
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            System.out.println("showReentrantLock complete.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

