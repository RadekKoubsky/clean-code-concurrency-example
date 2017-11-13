package org.rkoubsky.examples.dependenciesbetweenmethods.serverbasedlocking;

import org.rkoubsky.examples.dependenciesbetweenmethods.nonthreadsafe.IntegerIterator;

import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ThreadSafeClient {
    private final IntegerIteratorServerLocked iterator = new IntegerIteratorServerLocked();

    public void printIterator() {
        while (true) {
            final Integer nextValue = this.iterator.getNextOrNull();
            if (nextValue == null) {
                System.out.printf("Thread %s, Iterator returned null, it has reached the limit. \n",
                        Thread.currentThread().getId());
                break;
            }
            boolean finished = false;
            if (nextValue == IntegerIterator.MAX_SIZE - 1 && "first"
                    .equals(Thread.currentThread().getName())) {
                try {
                    System.out
                            .printf("Thread %s, Iterator has reached %s value, we want to wait for the other thread to" +
                                            "possibly break the iterator limit, but we use server based locking, thus" +
                                            " nothing bad happens. \n",
                                    Thread.currentThread().getId(), nextValue);
                    TimeUnit.MILLISECONDS.sleep(100);
                    finished = true;
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (finished) {
                System.out.printf("Thread %s, waiting finished.  \n",
                        Thread.currentThread().getId());
            }
            System.out
                    .printf("Thread %s, next value returned by iterator %s \n", Thread.currentThread().getId(),
                            nextValue);
        }
    }

    public IntegerIteratorServerLocked getIterator() {
        return this.iterator;
    }
}
