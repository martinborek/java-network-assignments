package com.interjava.hangman.server.connection;

import com.interjava.hangman.server.logic.PlayerState;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge
 */
public class HangmanProtocol {
    private static final String SEPARATOR = "|";
    public static final String START_GAME = "start game";
    public static final String CLOSE = "_CLOSE";
    public static final String ERROR = "ERROR";
    
    private PlayerState state;

    /**
     * Main method for interpreting a message, it receives a message from the client and it alters the game state accordingly
     * @param msg the message sent by the client
     * @return the message response
     */
    public String message(String msg) {
        
        if(START_GAME.equals(msg)) {
            return startGame();
        } else if(CLOSE.equals(msg)) {
            return CLOSE;
        } else if(msg.matches("[A-Za-z0-9&\\.']+")) {
            return attempt(msg.toLowerCase());
        } else {
            return ERROR;
        }
        
    }
    
    /**
     * initialises the game
     * @return the Initial state as a message to the client
     */
    private String startGame() {
        Integer currScore = state != null ? state.getScore() : 0;
        state = new PlayerState(currScore);
        return getProtocolString(state);
    }

    /**
     * Takes a letter or a word, it sends it to the game logic as an attempt and then returns the correct message accordingly to the new state
     * @param msg letter or word
     * @return new state after attempting the letter/word
     */
    private String attempt(String msg) {
        if(state == null) {return ERROR;}
        
        state.tryLetterOrWord(msg);
        return getProtocolString(state);
    }

    /**
     * translates the game state into the correct protocol string
     * @param state the state of the game
     * @return the string representation of the state
     */
    private String getProtocolString(PlayerState state) {
        
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(HangmanProtocol.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        String message;
        switch(state.gameResult()) {
            case PlayerState.LOSE:
                message = "Game over, you lose! The word was: "+state.getWord();
                break;
            case PlayerState.WIN:
                message = "Congratulations, you win! The word is: : "+state.getWord();
                break;
            default :
                message = "null";
        }
        
        return state.getTriedLetters()+SEPARATOR+state.getRemaningAttempts()+SEPARATOR+message+SEPARATOR+state.getScore();
    }
}
