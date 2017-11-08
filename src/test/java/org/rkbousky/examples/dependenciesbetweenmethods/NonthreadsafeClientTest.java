package org.rkbousky.examples.dependenciesbetweenmethods;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rkbousky.examples.dependenciesbetweenmethods.nonthreadsafe.IntegerIterator;
import org.rkbousky.examples.dependenciesbetweenmethods.nonthreadsafe.NonthreadSafeClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
@RunWith(Parameterized.class)
public class NonthreadsafeClientTest {

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[5][0]);
    }

    @Test
    public void iteratorShouldFailDuringConcurrentAccessTest() throws InterruptedException {
        final NonthreadSafeClient client = new NonthreadSafeClient();
        final List<Throwable> concurrentAccessExceptions = Collections.synchronizedList(new LinkedList<>());
        final Thread.UncaughtExceptionHandler handler = getUncaughtExceptionHandler(concurrentAccessExceptions);
        startThread(client, handler, "first");
        startThread(client, handler, "second");
        waitForIteratorEnd(client);
        System.out.println("Iterator value after the test: " + client.getIterator().getNextValue());
        Assertions.assertThat(concurrentAccessExceptions.isEmpty())
                .as("There has not been any concurrent access exception, try the test again to hit the corner case.")
                .isFalse();
        Assertions.assertThat(concurrentAccessExceptions)
                .as("There should be only one concurrent access exception thrown by the integer iterator.")
                .hasSize(1);
    }

    private Thread.UncaughtExceptionHandler getUncaughtExceptionHandler(
            final List<Throwable> concurrentAccessExceptions) {
        return (t, e) -> {
            System.out.printf("Exception in thread: id %s, name '%s' \n", t.getId(), t.getName());
            e.printStackTrace();
            concurrentAccessExceptions.add(e);
        };
    }

    private void startThread(
            final NonthreadSafeClient client, final Thread.UncaughtExceptionHandler handler, final String threadName) {
        final Thread thread = new Thread(client::printIterator, threadName);
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
    }

    private void waitForIteratorEnd(final NonthreadSafeClient client) throws InterruptedException {
        while (client.getIterator().getNextValue() != IntegerIterator.MAX_SIZE) {
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
