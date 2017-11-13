package org.rkoubsky.examples.dependenciesbetweenmethods.nonthreadsafe;

import java.util.Iterator;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class IntegerIterator implements Iterator<Integer> {
    public static final int MAX_SIZE = 100000;
    private Integer nextValue = 0;

    @Override
    public synchronized boolean hasNext() {
        return nextValue < MAX_SIZE;
    }

    @Override
    public synchronized Integer next() {
        if (nextValue == MAX_SIZE) {
            throw new IllegalStateException("Iterator has reached the end.");
        }
        return nextValue++;
    }

    public synchronized Integer getNextValue() {
        return nextValue;
    }
}
