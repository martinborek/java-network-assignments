package com.interjava.hangman.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Takes care of the main socket and creates every new connection
 * @author Jorge
 */
public class HangmanConnector {
    private static final int THREAD_POOL_SIZE = 10;
    public static final String VERSION = "v1.0";

    private static boolean listening = true;
    private static ServerSocket socket;

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                listening = false;
                if (socket != null) {
                    try {
                        socket.close();
                        System.out.println("Socket closed");
                    } catch (IOException ex) {
                        System.err.println("Error closing the server socket");
                        ex.printStackTrace();
                    }
                }
            }
        });

        int port = Integer.parseInt(args[0]);
        
        Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        try (ServerSocket socketTmp = new ServerSocket(port)) {
            socket = socketTmp;
            System.out.println("Waiting for connections");
            while (listening) {
                Socket clientSocket = socket.accept();
                executor.execute(new ClientThread(clientSocket));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("END");
    }
}
