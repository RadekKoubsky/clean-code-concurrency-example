package org.rkoubsky.examples.server.threaded;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.rkoubsky.examples.server.TrivialClient;
import org.rkoubsky.examples.server.threaded.connection.ConnectionManager;
import org.rkoubsky.examples.server.threaded.policy.ExecutorClientScheduler;
import org.rkoubsky.examples.server.threaded.policy.ThreadPerRequestClientScheduler;
import org.rkoubsky.examples.server.threaded.policy.ClientScheduler;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
@RunWith(Parameterized.class)
public class ClientServerThreadedSOLIDTest {
   private static final int PORT = 8010;
   private static final int TIMEOUT = 2000;
   private ServerThreadedSOLID serverThreadedSOLID;
   private Thread serverThread;

   @Parameterized.Parameters
   public static Iterable<? extends Object> data() {
      return Arrays.asList(new ThreadPerRequestClientScheduler(), new ExecutorClientScheduler(5));
   }

   @Parameterized.Parameter
   public ClientScheduler clientScheduler;

   @Before
   public void createServer() throws Exception {
      try {
         this.serverThreadedSOLID =
               new ServerThreadedSOLID(new ConnectionManager(PORT, TIMEOUT), this.clientScheduler);
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
