package org.rkoubsky.examples.server.threaded.policy;

import org.rkoubsky.examples.server.threaded.connection.ClientRequestProcessor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ExecutorClientScheduler implements ClientScheduler {
    private final Executor executor;

    public ExecutorClientScheduler(int availableThreads) {
        this.executor = Executors.newFixedThreadPool(availableThreads);
    }

    @Override
    public void schedule(final ClientRequestProcessor requestProcessor) {
        this.executor.execute(requestProcessor::process);
    }
}
