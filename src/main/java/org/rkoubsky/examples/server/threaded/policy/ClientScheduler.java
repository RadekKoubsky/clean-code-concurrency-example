package org.rkoubsky.examples.server.threaded.policy;

import org.rkoubsky.examples.server.threaded.connection.ClientRequestProcessor;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public interface ClientScheduler {
    void schedule(ClientRequestProcessor requestProcessor);
}
