package org.rkoubsky.examples.dependenciesbetweenmethods.serverbasedlocking;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class IntegerIteratorServerLocked {
    public static final int MAX_SIZE = 100000;
    private Integer nextValue = 0;

    public synchronized Integer getNextOrNull() {
        if (this.nextValue < MAX_SIZE) {
            return this.nextValue++;
        } else {
            return null;
        }
    }

    public synchronized Integer getNextValue() {
        return this.nextValue;
    }
}
