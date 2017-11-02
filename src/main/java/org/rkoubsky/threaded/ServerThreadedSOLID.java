package org.rkoubsky.threaded;

import org.rkoubsky.utils.MessageUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ServerThreadedSOLID implements  Runnable{
   private final ConnectionManager connectionManager;
   private final ClientScheduler clientScheduler;
   volatile boolean keepProcessing = true;

   public ServerThreadedSOLID(ConnectionManager connectionManager, ClientScheduler clientScheduler) throws IOException {
      this.connectionManager = connectionManager;
      this.clientScheduler = clientScheduler;
   }

   public void run() {
      System.out.println("Server Starting");

      while (this.keepProcessing) {
         try {
            ClientConnection clientConnection = connectionManager.awaitClient();
            ClientRequestProcessor requestProcessor = new ClientRequestProcessor(clientConnection);
            clientScheduler.schedule(requestProcessor);
         } catch (final IOException e) {
            handle(e);
         }
      }
   }

   private void handle(final IOException e) {
      if (!(e instanceof SocketException)) {
         System.out.printf("Socket timeout, thread %s, time %s \n", Thread.currentThread().getId(), LocalDateTime.now());
         e.printStackTrace();
      }
   }

   public void stopProcessing() {
      System.out.println("Shutting down server.");
      this.keepProcessing = false;
      connectionManager.closeServerSocket();
   }


}
