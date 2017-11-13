package org.rkoubsky.examples.dependenciesbetweenmethods.serverbasedlocking;

import org.rkoubsky.examples.dependenciesbetweenmethods.nonthreadsafe.IntegerIterator;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 *
 * An adapter for {@link IntegerIterator} class to make it server locked in case we do not
 * own the server code.
 */
public class ThreadSafeIntegerIteratorAdapter {
    private final IntegerIterator iterator = new IntegerIterator();

    public synchronized Integer getNextOrNull(){
        if(this.iterator.hasNext()){
            return this.iterator.next();
        }
        return null;
    }
}
