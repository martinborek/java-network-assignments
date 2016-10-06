package com.interjava.hangman.server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Thread to manage the connection with each client
 * @author Jorge
 */
public class ClientThread implements Runnable{
    
    private final Socket socket;
    private final HangmanProtocol protocol;
    
    ClientThread(Socket clientSocket) {
        socket = clientSocket;
        protocol = new HangmanProtocol();
    }

    /**
     * Receives the requests from the client, procecess the response with HangmanProtocol and replies the response
     */
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            
            out.println("Hangman "+HangmanConnector.VERSION);
            out.flush();
            
            String request;
            String response = "";
            while (!HangmanProtocol.CLOSE.equals(response) && (request = in.readLine()) != null) {
                response = protocol.message(request);
                if(!HangmanProtocol.CLOSE.equals(response)) {
                    out.println(response);
                    out.flush();
                } else {
                    out.println("connection closed");
                    out.flush();
                }
            }
            socket.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
