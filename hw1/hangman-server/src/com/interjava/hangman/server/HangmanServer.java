/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interjava.hangman.server;

import com.interjava.hangman.server.logic.PlayerState;

/**
 * Class used for testing the PlayerState
 * @author Jorge
 */
public class HangmanServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        System.out.println("word: "+RandomWordGenerator.generate());
        
        PlayerState game = new PlayerState(0);
        
        printState(game);
        
        System.out.println("Attempt: a");
        game.tryLetterOrWord("a");
        printState(game);
        
        System.out.println("Attempt: r");
        game.tryLetterOrWord("r");
        printState(game);
        
        System.out.println("Attempt: x");
        game.tryLetterOrWord("x");
        printState(game);
        
        System.out.println("Attempt: o");
        game.tryLetterOrWord("o");
        printState(game);
        
        System.out.println("Attempt: z");
        game.tryLetterOrWord("z");
        printState(game);
        
        System.out.println("Attempt: y");
        game.tryLetterOrWord("y");
        printState(game);
        
        System.out.println("Attempt: p");
        game.tryLetterOrWord("p");
        printState(game);
        
        System.out.println("Attempt: t");
        game.tryLetterOrWord("t");
        printState(game);
        
//        System.out.println("Attempt: k");
//        game.tryLetterOrWord("k");
//        printState(game);
        
        System.out.println("Attempt: n");
        game.tryLetterOrWord("n");
        printState(game);
        
        System.out.println("Attempt: d");
        game.tryLetterOrWord("d");
        printState(game);
        
        System.out.println("Attempt: w");
        game.tryLetterOrWord("w");
        printState(game);
        
        System.out.println("Attempt: m");
        game.tryLetterOrWord("m");
        printState(game);
        
    }

    private static void printState(PlayerState game) {
        System.out.println("Word: "+game.getWord());
        System.out.println("Current: "+game.getTriedLetters());
        System.out.println("Remaning Attempts: "+game.getRemaningAttempts());
        System.out.println("game resutl: "+game.gameResult());
        System.out.println("");
    }
    
}
