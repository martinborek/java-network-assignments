package com.interjava.hangman.client;

/**
 * Created by Jorge on 10/12/2015.
 */
public interface HangmanClient {

    public void updateEvent();
    public void connectErrorAction(String s);

    public GameStatus getGameStatus();
}
