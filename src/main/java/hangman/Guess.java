package hangman;

public class Guess {

    String guess;

    /**
     * Representation of a guess, allowing custom methods to act on it for
     * various purposes.
     * @param guess The letter guessed.
     */
    public Guess(String guess) {
        this.guess = guess.toUpperCase();
    }

    /**
     * Check if the guess is valid. It should only contain one letter. Anything
     * else will return false.
     * @return true if guess is a single character, false otherwise
     */
    public boolean isValid() {
        if (guess.length() == 1 && guess.matches("[A-Za-z]+")) {
            return true;
        }
        return false;
    }

    /**
     * Return the String representation of the guess.
     * @return
     */
    public String getGuess() {
        return guess;
    }

}
