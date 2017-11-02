package org.rkoubsky.threaded;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ThreadPerRequestScheduler implements ClientScheduler {
   @Override
   public void schedule(final ClientRequestProcessor requestProcessor) {
      Thread thread = new Thread(requestProcessor::process);
      thread.start();
   }
}
