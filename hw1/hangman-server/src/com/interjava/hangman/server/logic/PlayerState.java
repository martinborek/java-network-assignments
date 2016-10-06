package com.interjava.hangman.server.logic;

/**
 * Class that contains the game logic
 * @author Jorge
 */
public class PlayerState {

    private static final String NOT_GUESSED_CHAR = "-";
    private static final int INITIAL_ATTEMPTS = 6;
    public static final int WIN = 3;
    public static final int LOSE = 5;
    public static final int PLAYING = 8;

    private String word;
    private String triedLetters;
    private Integer remaningAttempts;
    private Integer score;
    private boolean playing;

    public PlayerState(Integer score) {
        word = RandomWordGenerator.generate();
//        word = "randomWord";
        final String anyLetterRegex = ".";
        triedLetters = word.replaceAll(anyLetterRegex, NOT_GUESSED_CHAR);
        remaningAttempts = INITIAL_ATTEMPTS;
        this.score = score;
        playing = true;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTriedLetters() {
        return triedLetters;
    }

    public void setTriedLetters(String triedLetters) {
        this.triedLetters = triedLetters;
    }

    public Integer getRemaningAttempts() {
        return remaningAttempts;
    }

    public void setRemaningAttempts(Integer remaningAttempts) {
        this.remaningAttempts = remaningAttempts;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
    
    /**
     * Changes the state accordingly to the letter/word tried
     * @param letter letter or word to try for the hangman
     */
    public void tryLetterOrWord(String letter) {
        if (letter == null) {
            return;
        }
        if (LOSE == gameResult()) {
            return;
        }

        StringBuilder triedLettersBuilder = new StringBuilder(triedLetters);
        //If it is a letter
        String wordTemp = word.toLowerCase();
        if (letter.length() == 1) {
            if (wordTemp.contains(letter)) {
                int position;
                //Replace all the ocurences of the letter in the string of dashes "-" (triedLetters)
                while ((position = wordTemp.indexOf(letter)) != -1) {
                    //Replace the dash with the original letter in the original word (In the correct case)
                    triedLettersBuilder.replace(position, position + 1, ""+word.charAt(position));
                    //rule out the letter
                    wordTemp = wordTemp.replaceFirst("(?i)"+letter, NOT_GUESSED_CHAR);
                }
                triedLetters = triedLettersBuilder.toString();
            } else {//Letter not included in the word
                remaningAttempts--;
            }
        } else if (letter.length() > 1) {//If it is a word
            if (letter.equalsIgnoreCase(word)) {
                triedLetters = word;
            } else {//wrong word
                remaningAttempts--;
            }
        }
        
        if(playing){
            switch(this.gameResult()) {
                case PlayerState.LOSE:
                    score--;
                    playing = false;
                    break;
                case PlayerState.WIN:
                    score++;
                    playing = false;
                    break;
            }
        }
    }

    /**
     * Returns the code for the game result
     * @return 
     */
    public int gameResult() {
        
        if(remaningAttempts <= 0) {
            return LOSE;
        }
        if(!triedLetters.contains(NOT_GUESSED_CHAR) && (remaningAttempts > 0)) {
            return WIN;
        } else {
            return PLAYING;
        }
    }
}
