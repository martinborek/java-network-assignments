/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.hangman.client;

/**
 * Class maintaining the game status.
 * 
 * @author Martin Borek (mborekcz@gmail.com)
 * @date 19/Nov/2015
 * @file GameStatus.java
 */
public class GameStatus {
    private int attempts;
    private String word;
    private String message;
    private Boolean loss;
    private Boolean win;
    private Integer score;

    GameStatus()
    {
        attempts = 10;
        word = "-----------";
        message = null;
        loss = false;
        win = false;
    }
    
    /**
     * Sets how many guess attempts are remaining.
     *
     * @param attempts The amount of remaining guess attempts.
     */
    public void setAttempts(int attempts) {
        this.attempts = attempts;

        if (attempts < 1)
            loss = true;
    }

    /**
     * Sets the current word status.
     *
     * @param word Current word (with dashes)
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Sets the win / lose message.
     *
     * @param message Win / lose message
     */
    public void setMessage(String message) {
        this.message = message;

        if (!"null".equals(message) && attempts > 0)
        {
            System.out.println("WON");
            win = true;
        }
    }

    /**
     * Returns how many guess attempts are remaining.
     *
     * @return attempts The amount of remaining guess attempts.
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Returns the current word status.
     *
     * @return word Current word (with dashes)
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the win / lose message.
     *
     * @return message Win / lose message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns info if the user won.
     *
     * @return Has won?
     */
    public Boolean isWin() {
        return win;
    }

    /**
     * Returns info if the user lost.
     *
     * @return Has lost?
     */
    public Boolean isLost() {
        return loss;
    }

    /**
     * Returns the current score (games won - games lost).
     *
     * @return Score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the current score (games won - games lost).
     * @param score Score
     */
    public void setScore(Integer score) {
        this.score = score;
    }
}
