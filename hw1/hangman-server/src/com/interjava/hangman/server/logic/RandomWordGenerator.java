package com.interjava.hangman.server.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

/**
 * Randomly selects a word from the text file
 * @author Jorge
 */
public class RandomWordGenerator {

    private static final String FILE_PATH = "./resources/words.txt";
    private static final int FILE_LINES_NUM = 25143;

    /**
     * Selects a random word
     * @return the selected word
     */
    public static String generate() {

        Random random = new Random();
        int rand = random.nextInt(FILE_LINES_NUM)+1;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = br.readLine();
            for (int i = 1; i < rand; i++) {
                line = br.readLine();
            }
            return line;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
