package org.rkoubsky.threaded;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public interface ClientScheduler {
   void schedule(ClientRequestProcessor requestProcessor);
}
