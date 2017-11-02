package org.rkoubsky.threaded;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rkoubsky.TrivialClient;

import java.io.IOException;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ClientServerThreadedSOLIDTest {
   private static final int PORT = 8010;
   private static final int TIMEOUT = 2000;
   private ServerThreadedSOLID serverThreadedSOLID;
   private Thread serverThread;

   @Before
   public void createServer() throws Exception {
      try {
         this.serverThreadedSOLID =
               new ServerThreadedSOLID(new ConnectionManager(PORT, TIMEOUT), new ThreadPerRequestScheduler());
         this.serverThread = new Thread(this.serverThreadedSOLID);
         this.serverThread.start();
      } catch (final IOException e) {
         e.printStackTrace(System.err);
         throw e;
      }
   }

   @After
   public void shutdownServer() throws InterruptedException {
      if (this.serverThreadedSOLID != null) {
         this.serverThreadedSOLID.stopProcessing();
         this.serverThread.join();
      }
   }

   @Test(timeout = 10000)
   public void shouldRunInUnder10Seconds() throws Exception {
      final Thread[] threads = new Thread[10];
      for (int i = 0; i < 10; i++) {
         threads[i] = new Thread(new TrivialClient(i, PORT));
         threads[i].start();
      }

      for (int i = 0; i < 10; i++) {
         threads[i].join();
      }
   }
}
