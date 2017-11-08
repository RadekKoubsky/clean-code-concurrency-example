package org.rkoubsky.examples.server.nonthreaded;

import org.rkoubsky.examples.server.utils.MessageUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private volatile boolean keepProcessing = true;

    public Server(final int port, final int millisecondsTimeout) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setSoTimeout(millisecondsTimeout);
    }

    public void run() {
        System.out.println("Server Starting");

        while (this.keepProcessing) {
            try {
                System.out.println("accepting client");
                final Socket socket = this.serverSocket.accept();
                System.out.println("got client");
                process(socket);
            } catch (final IOException e) {
                handle(e);
            }
        }
    }

    private void handle(final IOException e) {
        if (!(e instanceof SocketException)) {
            e.printStackTrace();
        }
    }

    private void process(final Socket socket) {
        if (socket == null) {
            return;
        }

        try {
            final long threadId = Thread.currentThread().getId();
            System.out.printf("Server thread %2d: getting message\n", threadId);
            final String message = MessageUtils.getMessage(socket);
            System.out.printf("Server thread %2d: got message: %s\n", threadId, message);
            TimeUnit.SECONDS.sleep(1);
            System.out.printf("Server thread %2d: sending reply: From Server %2d\n", threadId, threadId);
            MessageUtils.sendMessage(socket, "From Server " + threadId);
            System.out.printf("Server thread %2d: sent\n", threadId);
            closeIgnoringException(socket);
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    public void stopProcessing() {
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
