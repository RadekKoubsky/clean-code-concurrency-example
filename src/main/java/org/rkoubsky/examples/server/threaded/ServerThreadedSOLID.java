package org.rkoubsky.examples.server.threaded;

import org.rkoubsky.examples.server.threaded.connection.ClientConnection;
import org.rkoubsky.examples.server.threaded.connection.ClientRequestProcessor;
import org.rkoubsky.examples.server.threaded.connection.ConnectionManager;
import org.rkoubsky.examples.server.threaded.policy.ClientScheduler;

import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDateTime;

/**
 * @author Radek Koubsky (radekkoubsky@gmail.com)
 */
public class ServerThreadedSOLID implements Runnable {
    private final ConnectionManager connectionManager;
    private final ClientScheduler clientScheduler;
    volatile boolean keepProcessing = true;

    public ServerThreadedSOLID(ConnectionManager connectionManager,
            ClientScheduler clientScheduler) throws IOException {
        this.connectionManager = connectionManager;
        this.clientScheduler = clientScheduler;
    }

    public void run() {
        System.out.println("Server Starting");

        while (this.keepProcessing) {
            try {
                ClientConnection clientConnection = this.connectionManager.awaitClient();
                ClientRequestProcessor requestProcessor = new ClientRequestProcessor(clientConnection);
                this.clientScheduler.schedule(requestProcessor);
            } catch (final IOException e) {
                handle(e);
            }
        }
    }

    private void handle(final IOException e) {
        if (!(e instanceof SocketException)) {
            System.out.printf("Socket timeout, thread %s, time %s \n", Thread.currentThread().getId(),
                    LocalDateTime.now());
            e.printStackTrace();
        }
    }

    public void stopProcessing() {
        System.out.println("Shutting down server.");
        this.keepProcessing = false;
        this.connectionManager.closeServerSocket();
    }


}
