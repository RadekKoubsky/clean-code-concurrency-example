package org.rkoubsky.threaded;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ConnectionManager {
   private final ServerSocket serverSocket;

   public ConnectionManager(final int port, final int millisecondsTimeout) throws IOException {
      this.serverSocket = new ServerSocket(port);
      this.serverSocket.setSoTimeout(millisecondsTimeout);
   }

   public ClientConnection awaitClient() throws IOException {
      System.out.printf("Accepting client, thread %s, time %s \n", Thread.currentThread().getId(), LocalDateTime.now());
      ClientConnection clientConnection = new ClientConnection(this.serverSocket.accept());
      System.out.printf("Got client, thread %s, time %s \n", Thread.currentThread().getId(), LocalDateTime.now());
      return clientConnection;
   }

   public void closeServerSocket() {
      closeIgnoringException();
   }

   private void closeIgnoringException() {
      if (this.serverSocket != null) {
         try {
            this.serverSocket.close();
         } catch (final IOException e) {
         }
      }
   }
}
