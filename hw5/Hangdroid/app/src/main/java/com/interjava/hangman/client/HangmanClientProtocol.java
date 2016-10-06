package com.interjava.hangman.client;

import java.util.StringTokenizer;

/**
 * Protocol for communication with the server
 *
 * @author Martin Borek (mborekcz@gmail.com)
 * @date 19/Nov/2015
 * @file HangmanClientConnection.java
 */
public class HangmanClientProtocol {

    private static final String SEPARATOR = "|";

    /**
     * Message for starting a new game.
     */
    public static final String START_GAME = "start game";

    /**
     * Message for closing the server connection.
     */
    public static final String CLOSE = "_CLOSE";



    /**
     * Message for starting a new game.
     *
     * @return Start game message
     */
    static public String startGame()
    {
        return START_GAME;
    }

    /**
     * Message for closing the server connection. 
     *
     * @return Close connection message
     */
    static public String close()
    {
        return CLOSE;
    }

    /**
     * Decodes received message (game status).
     *
     * @param message Received message from the server
     * @param status Object for storing the decoded information
     */
    static public void decode(String message, GameStatus status)
    {
        //todo: check if values are correct

        StringTokenizer st = new StringTokenizer(message, SEPARATOR);
        status.setWord(st.nextElement().toString());
        status.setAttempts(Integer.parseInt(st.nextElement().toString()));
        status.setMessage(st.nextElement().toString());
        status.setScore(Integer.parseInt(st.nextElement().toString()));
    }
}