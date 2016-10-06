/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.hangman.client;

import java.io.BufferedReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Class handling connection to the game server for a Hangman client. 
 * 
 * @author Martin Borek (mborekcz@gmail.com)
 * @date 19/Nov/2015
 * @file HangmanClientConnection.java
 */
public class HangmanClientConnection implements Runnable {

    private final HangmanClient client;
    private final String server;
    private final int port;
    
    private String character;

    private Boolean sendCharacterFlag;
    private Boolean newGameFlag;

    private BufferedReader input;
    private PrintWriter output;

    private Socket clientSocket;
    
    HangmanClientConnection(HangmanClient client, String server, int port)
    {

        this.client = client;
        this.server = server;
        this.port = port;
        sendCharacterFlag = false;
        newGameFlag = false;
    }

    /**
     * Running a task in a separate thread. The task is determined by set flags.
     */
    @Override
    public void run() {
        if (newGameFlag)
            newGame();
        else if (sendCharacterFlag)
            sendCharacter(character);
        else
            connectToServer();
    }

    /**
     * Establishes a connection with the server and starts a new game.
     */
    private void connectToServer() { 
        
        try {
            clientSocket = new Socket(server, port);
            clientSocket.setSoTimeout(10000); //timeout 10 sec

            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream());

            String receivedMessage = input.readLine();
            System.out.println(receivedMessage);
            output.println(HangmanClientProtocol.startGame());
            output.flush();

            receivedMessage = input.readLine();

            // decode received game status and fill this info in the client's gameStatus
            HangmanClientProtocol.decode(receivedMessage, client.gameStatus);

            client.updateEvent();
        } catch (UnknownHostException e) {
            client.connectErrorAction("Could not find server " + server + ".");
        } catch (IOException e) {
            client.connectErrorAction("Could not connect to server " + server + ".");
        } catch (Exception e) {
            client.connectErrorAction("Connecting to server " + server + " failed.");
        } 
    }

    /**
     * Sends a guessed character (or a guessed word) to the server, decodes the response and updates the game status.
     * 
     * @param guessedCharacter A guessed character or a guessed word
     */
    private void sendCharacter(String guessedCharacter) {
        try {
            output.println(guessedCharacter);
            output.flush();
            String receivedMessage = input.readLine();
            HangmanClientProtocol.decode(receivedMessage, client.gameStatus);
            client.updateEvent();
        } catch (IOException ex) {
            System.err.println("Error when guessing a letter.");
        }
    }

    /**
     * Creates a new game.
     */
    private void newGame()
    {
        try {
            output.println(HangmanClientProtocol.startGame());
            output.flush();
            String receivedMessage = input.readLine();
            HangmanClientProtocol.decode(receivedMessage, client.gameStatus);
            client.updateEvent();
        } catch (IOException ex) {
            System.err.println("Error when guessing a letter.");
        }
    }

    /**
     * Sets flag for starting a new game.
     */
    public void setNewGameFlag()
    {
        sendCharacterFlag = false;
        newGameFlag = true;
    }

    /**
     * Sets flag for sending a character (or a word).
     *
     * @param character
     */
    public void setSendCharacterFlag(String character)
    {
        this.character = character;
        sendCharacterFlag = true;
        newGameFlag = false;
    }

    /**
     * Disconnects the socket. It is used for interrupting the thread when an IO operation shall be aborted. 
     */
    public void disconnect()
    {

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
