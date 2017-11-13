package org.rkoubsky.examples.dependenciesbetweenmethods;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rkoubsky.examples.dependenciesbetweenmethods.serverbasedlocking.IntegerIteratorServerLocked;
import org.rkoubsky.examples.dependenciesbetweenmethods.serverbasedlocking.ThreadSafeClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
@RunWith(Parameterized.class)
public class ServerBasedLockingTest {
    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[5][0]);
    }

    @Test
    public void iteratorShouldNotFailDuringConcurrentAccessTest() throws InterruptedException {
        final ThreadSafeClient client = new ThreadSafeClient();
        final List<Throwable> concurrentAccessExceptions = Collections.synchronizedList(new LinkedList<>());
        final Thread.UncaughtExceptionHandler handler = getUncaughtExceptionHandler(concurrentAccessExceptions);
        startThread(client, handler, "first");
        startThread(client, handler, "second");
        waitForIteratorEnd(client);
        System.out.println("Iterator value after the test: " + client.getIterator().getNextValue());
        Assertions.assertThat(concurrentAccessExceptions.isEmpty())
                .as("There should not be any concurrent access exception as we use server based locking properly.")
                .isTrue();
    }

    private Thread.UncaughtExceptionHandler getUncaughtExceptionHandler(
            final List<Throwable> concurrentAccessExceptions) {
        return (t, e) -> {
            System.out.printf("Exception in thread: id %s, name '%s' \n", t.getId(), t.getName());
            e.printStackTrace();
            concurrentAccessExceptions.add(e);
        };
    }

    private void startThread(final ThreadSafeClient client, final Thread.UncaughtExceptionHandler handler,
            final String threadName) {
        final Thread thread = new Thread(client::printIterator, threadName);
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
    }

    private void waitForIteratorEnd(final ThreadSafeClient client) throws InterruptedException {
        while (client.getIterator().getNextValue() != IntegerIteratorServerLocked.MAX_SIZE) {
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
