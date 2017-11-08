package org.rkoubsky.examples.server.nonthreaded;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rkoubsky.examples.server.TrivialClient;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ClientServerNonThreadedTest {
    private static final int PORT = 8009;
    private static final int TIMEOUT = 1000;

    Server server;
    Thread serverThread;

    @Before
    public void createServer() throws Exception {
        try {
            this.server = new Server(PORT, TIMEOUT);
            this.serverThread = new Thread(this.server);
            this.serverThread.start();
        } catch (final IOException e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @After
    public void shutdownServer() throws InterruptedException {
        if (this.server != null) {
            this.server.stopProcessing();
            this.serverThread.join();
        }
    }

    @Test(timeout = 20000)
    public void shouldRunInUnder10Seconds() throws Exception {
        final Thread[] threads = new Thread[10];
        IntStream.range(0, 10).forEach(threadId -> {
            threads[threadId] = new Thread(new TrivialClient(threadId, PORT));
            threads[threadId].start();
        });

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }
    }
}
