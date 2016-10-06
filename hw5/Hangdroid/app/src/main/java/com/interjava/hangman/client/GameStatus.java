package com.interjava.hangman.client;

import java.io.Serializable;

/**
 * Class maintaining the game status.
 *
 * @author Martin Borek (mborekcz@gmail.com)
 * @date 19/Nov/2015
 * @file GameStatus.java
 */
public class GameStatus implements Serializable{
    private int attempts;
    private String word;
    private String message;
    private Boolean loss;
    private Boolean win;
    private Integer score;

    public GameStatus() {
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

        if (!"null".equals(message) && attempts > 0) {
            System.out.println("WON");
            win = true;
        }
    }

    public String GetMessage(){
        return message;
    }

    public int getAttempts() {
        return attempts;
    }

    public String getWord() {
        return word;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getLoss() {
        return loss;
    }

    public void setLoss(Boolean loss) {
        this.loss = loss;
    }

    public Boolean getWin() {
        return win;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isLost() {
        return loss;
    }

    public boolean isLoss() {
        return loss;
    }
}
