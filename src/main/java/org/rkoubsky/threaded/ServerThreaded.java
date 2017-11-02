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
 *
 * Server which runs each request in a separate thread.
 */
public class ServerThreaded implements Runnable {
   private final ServerSocket serverSocket;
   volatile boolean keepProcessing = true;

   public ServerThreaded(final int port, final int millisecondsTimeout) throws IOException {
      this.serverSocket = new ServerSocket(port);
      this.serverSocket.setSoTimeout(millisecondsTimeout);
   }

   public void run() {
      System.out.println("Server Starting");

      while (this.keepProcessing) {
         try {
            System.out.printf("Accepting client, thread %s, time %s \n", Thread.currentThread().getId(), LocalDateTime.now());
            final Socket socket = this.serverSocket.accept();
            System.out.printf("Got client, thread %s, time %s \n", Thread.currentThread().getId(), LocalDateTime.now());
            process(socket);
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

   private void process(final Socket socket) {
      if (socket == null) {
         return;
      }

      final Runnable clientHandler = new Runnable() {
         public void run() {
            try {
               final long threadId = Thread.currentThread().getId();
               System.out.printf("Server thread %2d: getting message\n", threadId);
               final String message = MessageUtils.getMessage(socket);
               System.out.printf("Server thread %2d: got message: %s\n", threadId, message);
               TimeUnit.SECONDS.sleep(1);
               System.out.printf("Server thread %2d: sending reply: From Server %2d\n", threadId, threadId);
               MessageUtils.sendMessage(socket, "From Server " + threadId);
               System.out.printf("%s Server thread %2d: sent\n", LocalDateTime.now(), threadId);
               closeIgnoringException(socket);
            } catch (final Exception e) {
               e.printStackTrace();
            }
         }
      };

      final Thread clientConnection = new Thread(clientHandler);
      clientConnection.start();

   }

   public void stopProcessing() {
      System.out.println("Shutting down server.");
      this.keepProcessing = false;
      closeIgnoringException(this.serverSocket);
   }

   private void closeIgnoringException(final Socket socket) {
      if (socket != null) {
         try {
            socket.close();
         } catch (final IOException e) {
         }
      }
   }

   private void closeIgnoringException(final ServerSocket serverSocket) {
      if (serverSocket != null) {
         try {
            serverSocket.close();
         } catch (final IOException e) {
         }
      }
   }
}
