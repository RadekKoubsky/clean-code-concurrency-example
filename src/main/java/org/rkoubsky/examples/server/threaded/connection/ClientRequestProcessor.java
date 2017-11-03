package org.rkoubsky.examples.server.threaded.connection;

import org.rkoubsky.examples.server.utils.MessageUtils;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ClientRequestProcessor {
   private final ClientConnection clientConnection;
   public ClientRequestProcessor(ClientConnection clientConnection) {
      this.clientConnection = clientConnection;
   }

   public void process() {
      try {
         final long threadId = Thread.currentThread().getId();
         System.out.printf("Server thread %2d: getting message\n", threadId);
         final String message = MessageUtils.getMessage(this.clientConnection.getSocket());
         System.out.printf("Server thread %2d: got message: %s\n", threadId, message);
         TimeUnit.SECONDS.sleep(1);
         System.out.printf("Server thread %2d: sending reply: From Server %2d\n", threadId, threadId);
         MessageUtils.sendMessage(this.clientConnection.getSocket(), "From Server " + threadId);
         System.out.printf("%s Server thread %2d: sent\n", LocalDateTime.now(), threadId);
         closeIgnoringException(this.clientConnection.getSocket());
      } catch (final Exception e) {
         e.printStackTrace();
      }
   }

   private void closeIgnoringException(final Socket socket) {
      if (socket != null) {
         try {
            socket.close();
         } catch (final IOException e) {
         }
      }
   }
}
