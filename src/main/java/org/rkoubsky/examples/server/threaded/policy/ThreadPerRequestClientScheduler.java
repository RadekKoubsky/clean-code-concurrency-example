package org.rkoubsky.examples.server.threaded.policy;

import org.rkoubsky.examples.server.threaded.connection.ClientRequestProcessor;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ThreadPerRequestClientScheduler implements ClientScheduler {
   @Override
   public void schedule(final ClientRequestProcessor requestProcessor) {
      Thread thread = new Thread(requestProcessor::process);
      thread.start();
   }
}
