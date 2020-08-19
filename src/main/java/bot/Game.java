package bot;

import hangman.RandomWord;
import hangman.Word;
import swing.LoginWindow;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Creates a ready-to-start instance of HangmanIK.
 *
 * @author Chris Jardine
 * @version 1.0
 */
public class Game {

    Client client;
    public static Word playingWord;

    public static boolean gameIsRunning;

    /**
     * Default constructor connects to the <code>client</code> and sets the
     * <code>gameIsRunning</code> variable to <code>false</code>. This will
     * be set to <code>true</code> when the game is actually started via one
     * of the methods.
     * @param client
     */
    public Game(Client client) {
        this.client = client;
        gameIsRunning = false;
    }

    /**
     * Starts a game with a custom word entered by the bot owner.
     */
    public void chooseWord() {
        // Initialize the label to be printed on the JOptionPane
        JLabel label = new JLabel("Enter a word:");
        label.setFont(LoginWindow.montserrat);
        label.setForeground(Color.white);
        // Store input from the text box
        String word = JOptionPane.showInputDialog(null, label, "Set Word", JOptionPane.PLAIN_MESSAGE);
        // word = null when cancel is pressed
        if (word != null) {
            // Change label for confirmation dialog box
            word = word.toUpperCase();
            label.setText("The word will be: " + word);
            // Create dialog window confirming the word.
            // If "yes"
            if (JOptionPane.showConfirmDialog(null,
                    label, "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
                // Set the game word to the entered word and start the game.
                playingWord = new Word(word);
                client.getClient().channelMessage(client.getClient().getNick()
                        + " has started HangmanIK! The word is: "
                        + playingWord.toString());
                gameIsRunning = true;
            }
//            // Otherwise, "no" was pressed.
        }
        // Otherwise, "cancel was pressed.
    }

    /**
     * Starts a game with a random word. The bot owner can continually generate
     * a new word until they find one they like and then start the game, or can
     * start the game on the first one.
     */
    public void randomWord() {
        // Initialize the label to be printed on the JOptionPane
        JLabel label = new JLabel();
        label.setFont(LoginWindow.montserrat);
        label.setForeground(Color.white);

        try {
            // Generate random word
            RandomWord rw = new RandomWord();
            rw.generateWord();
            // Set the game word the the generated word
            playingWord = new Word(rw.getWord().toUpperCase());
            // Update the label for the JOptionPane
            label.setText("Random word: " + rw.getWord());
            /*
            Create options for the dialog box.
            Let's Play! -> Start game
            New Word -> Generate new word
            Cancel
             */
            Object[] options = {
                    "Let's Play!",
                    "New Word",
                    "Cancel"
            };
            // Create the dialog box.
            int n = JOptionPane.showOptionDialog(LoginWindow.frame,
                    label, "Word Generator", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            // Actions based on selection
            // Let's Play! = 0;
            if (n == 0) {
                client.getClient().channelMessage(client.getClient().getNick() + " has started HangmanIK! The word is: " + playingWord.toString());
                gameIsRunning = true;

            }
            // New Word = 1
            else if (n == 1) {
                randomWord();
            }
            // Otherwise, cancel was pressed.

            // Catch errors
        } catch (FileNotFoundException e) {
            // Update the label and display an error if the file wasnt found.
            // If you see this, make sure the location to dictionary.txt hasn't
            // been changed. /src/main/java/resources/dictionary.txt
            label.setText("ERROR: dictionary.txt not found!");
            JOptionPane.showMessageDialog(null, label);
        } catch (IOException e) {
            // Update the label and display if an IOException was thrown. This
            // is thrown when reading lines in the RandomAccessFile and would
            // indicate a bug in the code if it's thrown.
            label.setText("ERROR: IOException!");
            JOptionPane.showMessageDialog(null, label);

        }
    }
}