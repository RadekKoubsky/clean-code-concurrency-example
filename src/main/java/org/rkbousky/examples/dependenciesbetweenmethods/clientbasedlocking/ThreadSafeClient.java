package org.rkbousky.examples.dependenciesbetweenmethods.clientbasedlocking;

import org.rkbousky.examples.dependenciesbetweenmethods.nonthreadsafe.IntegerIterator;

import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ThreadSafeClient {
    private final IntegerIterator iterator = new IntegerIterator();

    public void printIterator() {
        while (true) {
            final Integer nextValue;
            synchronized (this.iterator) {
                if (!this.iterator.hasNext()) {
                    break;
                }
                boolean finished = false;
                if (this.iterator.getNextValue() == IntegerIterator.MAX_SIZE - 1 && "first"
                        .equals(Thread.currentThread().getName())) {
                    try {
                        System.out
                                .printf("Thread %s, Iterator has reached %s value, we want to wait for the other thread to increase " +
                                                "the next value of the iterator to max size and possibly breaking the limit of the" +
                                                " iterator from within this thread by calling iterator.next(), but we locked " +
                                                "the iterator object using client based locking, thus the other thread does not" +
                                                "call iterator.next(), but it will wait for this thread to complete its code... \n",
                                        Thread.currentThread().getId(), this.iterator.getNextValue());
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
                nextValue = this.iterator.next();
            }
            System.out
                    .printf("Thread %s, next value returned by iterator %s \n", Thread.currentThread().getId(),
                            nextValue);
        }
    }

    public IntegerIterator getIterator() {
        return this.iterator;
    }
}
