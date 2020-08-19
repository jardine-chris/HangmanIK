package hangman;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * An object representing a random word generated from the dictionary.txt file.
 *
 * @author Chris Jardine
 * @version 1.0
 */
public class RandomWord {

    private final RandomAccessFile raf;

    String word;

    /**
     * Default constructors initializes the <code>RandomAccessFile</code>
     * variable.
     * The <i>dictionary.txt</i> file can be found and updated in the
     * <i>src/main/java/resources</i> folder. It <b>must</b> remain here.
     * @throws FileNotFoundException When the file was not found.
     */
    public RandomWord() throws FileNotFoundException {
        raf = new RandomAccessFile("src/main/resources/dictionary.txt", "r");
    }

    /**
     * Generate the word by getting the number of lines in the file and
     * generating a number based off of the result.
     * @throws IOException When a line can't be read.
     */
    public void generateWord() throws IOException {
        // Use helper method to determine number of lines.
        int numLines = numberOfLines();
        // Generate a random number between 1 and the number of lines.
        int position = (int) (Math.random() * (numLines - 1) + 1);

        // Move to the beginning of the file.
        raf.seek(0);
        // Loop through the document until reaching the random line.
        for (int i = 0; i < position; i++) {
            raf.readLine();
        }
        // Set the word to the random line.
        word = raf.readLine();
    }

    /**
     * Helper method to determine the number of lines in the
     * <i>dictionary.txt</i> file.
     * @return The number of lines in the file as an integer.
     */
    private int numberOfLines() {
        int lines = 0;
        try {
            while (raf.readLine() != null) {
                lines += 1;
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get the random word.
     * @return The random word.
     */
    public String getWord() {
        return word;
    }
}
