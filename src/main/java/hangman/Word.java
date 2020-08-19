package hangman;

import java.util.ArrayList;

/**
 * Representation of the word being used in the game.
 *
 * @author Chris Jardine
 * @version 1.0
 */
public class Word {

    private String word;
    private ArrayList<String> wordArray;
    private ArrayList<String> guessedLetters;

    /**
     * Default constructor stores the word the bot owner chose and
     * creates an array of underscores to represent the unguessed
     * letters in chat. Also initializes the <code>guessedLetters</code>
     * ArrayList.
     * @param word The word being used in the current instance of the game.
     */
    public Word(String word) {
        this.word = word;
        wordArray = new ArrayList<String>();
        guessedLetters = new ArrayList<String>();

        // Create array of underscores to represent the word in chat.
        for (char c : word.toCharArray()) {
            // Allow words with hyphens to show a hyphen rather than an
            // underscore.
            if (c == '-') {
                wordArray.add("-");
            }
            // Replace characters with an underscore.
            else if (c != ' ') {
                wordArray.add("_");
            }
            // Add a space (" ") to make multiple words clear.
            else {
                wordArray.add(" ");
            }
        }
    }

    /**
     * Checks the guess against the word if the guess is valid. If it is, the
     * word is searched for matching letters. The wordArray is updated,
     * replacing matching characters. Each matching character
     * @param g
     * @return
     */
    public int checkGuess(Guess g) {
        String guess = g.getGuess();
        int counter = 0;
        // Validate the guess
        if (g.isValid() && !checkIfGuessed(guess)) {
            // Replace underscores in word with their letter if the guessed
            // letter is in the word.
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess.toCharArray()[0]) {
                    wordArray.set(i, guess);
                    guessedLetters.add(guess);
                    counter += 1;
                }
            }
        }
        return counter;
    }

    /**
     * Check if the letter has already been guessed.
     * @param guess The letter guessed.
     * @return true if the letter has been guessed, false otherwise.
     */
    public boolean checkIfGuessed(String guess) {
        return guessedLetters.contains(guess);
    }

    /**
     * Check if the guess is the same as the word being played.
     * @param guess The word guessed.
     * @return true if the user guessed the word, false otherwise.
     */
    public boolean checkSolve(String guess) {
        if (guess.equalsIgnoreCase(word)) {
            return true;
        }
        return false;
    }

    /**
     * Get the word being played.
     * @return The word being played.
     */
    public String getWord() {
        return word;
    }

    /**
     * String representation of the word to be printed in the chat.
     * @return The ArrayList as a readable String.
     */
    public String toString() {
        String output = "";
        for (String c : wordArray) {
            output += c;
        }
        return output;
    }

}
