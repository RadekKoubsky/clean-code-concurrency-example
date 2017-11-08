package org.rkbousky.examples.dependenciesbetweenmethods.nonthreadsafe;

import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class NonthreadSafeClient {
    private final IntegerIterator iterator = new IntegerIterator();

    public void printIterator() {
        while (this.iterator.hasNext()) {
            boolean finished = false;
            if (this.iterator.getNextValue() == IntegerIterator.MAX_SIZE - 1 && "first"
                    .equals(Thread.currentThread().getName())) {
                try {
                    System.out
                            .printf("Thread %s, Iterator has reached %s value, waiting for other thread to increase " +
                                            "the next value of the iterator to max size and possibly breaking the limit of the" +
                                            " iterator from within this thread by calling iterator.next()... \n",
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
            final Integer nextValue = this.iterator.next();
            System.out
                    .printf("Thread %s, next value returned by iterator %s \n", Thread.currentThread().getId(),
                            nextValue);
        }
    }

    public IntegerIterator getIterator() {
        return this.iterator;
    }
}
