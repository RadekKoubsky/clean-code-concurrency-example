package org.rkoubsky.examples.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class MessageUtils {
   public static void sendMessage(final Socket socket, final String message) throws IOException {
      final OutputStream socketOutputStream = socket.getOutputStream();
      final ObjectOutputStream ois = new ObjectOutputStream(socketOutputStream);
      ois.writeUTF(message);
      ois.flush();
   }

   public static String getMessage(final Socket socket) throws IOException {
      final InputStream socketInputStream = socket.getInputStream();
      final ObjectInputStream ois = new ObjectInputStream(socketInputStream);
      return ois.readUTF();
   }
}
