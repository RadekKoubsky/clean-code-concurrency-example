package org.rkoubsky;

import org.rkoubsky.utils.MessageUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Radek Koubsky (rkoubsky@redhat.com)
 */
public class TrivialClient implements Runnable {
   private final int clientNumber;
   private final int port;

   public TrivialClient(final int clientNumber, final int port) {
      this.clientNumber = clientNumber;
      this.port = port;
   }

   public void run() {
      try {
         connectAndReceive();
      } catch (final IOException e) {
         e.printStackTrace();
      }
   }

   private void connectAndReceive() throws IOException {
      System.out.printf("Client %2d: connecting\n", this.clientNumber);
      final Socket socket = new Socket("localhost", this.port);
      MessageUtils.sendMessage(socket, "From client " + Integer.toString(this.clientNumber));
      System.out.printf("Client %2d, getting reply: %s \n", this.clientNumber, MessageUtils.getMessage(socket));
      socket.close();
   }
}
