package bot;

import com.gikk.twirk.Twirk;
import com.gikk.twirk.events.TwirkListener;
import com.gikk.twirk.types.twitchMessage.TwitchMessage;
import com.gikk.twirk.types.users.TwitchUser;
import hangman.Guess;

/**
 * The commands that the client listens for to play the game.
 *
 * @author Chris Jardine
 * @version 1.0
 */
public class Commands implements TwirkListener {

    Twirk client;

    String[] commands = {
            "guess",
            "solve"
    };

    /**
     * Default consntructor connects to the client.
     * @param client The client.
     */
    public Commands(Twirk client) {
        this.client = client;
    }

    /**
     * Listen for PRIVMSG tags.
     * @param sender The sender of the message.
     * @param message The message that was sent.
     */
    @Override
    public void onPrivMsg(TwitchUser sender, TwitchMessage message) {
        String msg = message.getContent().toLowerCase();
        if (msg.startsWith("!hm")) {
            if (Game.gameIsRunning) {
                // Parse the message that was sent.
                String[] parsedMessage = cleanParse(message.getContent());
                // Verify the command is valid.
                if (validCommand(parsedMessage)) {
                    // Create a Guess object from the parsed message.
                    Guess guess = new Guess(parsedMessage[2]);

                    // React based on what the command was (after !hm).
                    switch (parsedMessage[1].toLowerCase()) {
                        // If the user is guessing a letter.
                        case "guess":
                            // If the letter hasn't been guessed yet.
                            if (!Game.playingWord.checkIfGuessed(guess.getGuess())) {
                                // Keep track of how many occurences of the guessed
                                // letter there are.
                                int numRight = Game.playingWord.checkGuess(guess);
                                // If 1 instance, send a singular statement.
                                if (numRight == 1) {
                                    client.channelMessage("There is 1 "
                                            + guess.getGuess() + "! "
                                            + Game.playingWord.toString());
                                    Game.db.addCorrectGuesses(sender.getUserName(), 1);
                                }
                                // If multiple instances, send a plural statement.
                                else if (numRight >= 2) {
                                    client.channelMessage("There are " + numRight
                                            + " " + guess.getGuess() + "s! "
                                            + Game.playingWord.toString());
                                    Game.db.addCorrectGuesses(sender.getUserName(), numRight);
                                }

                                // Otherwise the letter wasn't in the word.
                                else {
                                    client.channelMessage(guess.getGuess()
                                            + " isn't in the word!");
                                    Game.db.addInorrectGuesses(sender.getUserName());
                                }
                            }
                            // Otherwise the letter has already been guessed.
                            else {
                                client.channelMessage(guess.getGuess()
                                        + " has already been guessed!");
                            }
                            break;

                        // If the user is trying to guess the word.
                        case "solve":

                            boolean isRight = Game.playingWord.checkSolve(parsedMessage[2]);
                            if (isRight) {
                                client.channelMessage(sender.getDisplayName()
                                        + " guessed the word '"
                                        + Game.playingWord.getWord() + "'!");
                                Game.db.addGamesWon(sender.getUserName());
                                Game.gameIsRunning = false;
                            } else {
                                client.channelMessage("'" + parsedMessage[2] + "'"
                                        + " is incorrect!");
                                Game.db.addInorrectGuesses(sender.getUserName());
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            // Otherwise the game isn't running.
            else {
                client.channelMessage("A HangmanIK game isn't being played! Ask " +
                        "the host to start it :)");
            }
        }

    }

    /**
     * Trims the leading and trailing spaces and parses the message at spaces.
     * @param message The message from Twitch chat.
     * @return The parsed message as an array.
     */
    private String[] cleanParse(String message) {
        String msg = message.toLowerCase().trim();
        String[] parsedMessage = msg.split(" ");
        // If guess contains multiple words
        if (parsedMessage[1].equalsIgnoreCase("solve")
                && parsedMessage.length > 3) {
            msg = "";
            for (int i = 2; i < parsedMessage.length - 1; i++) {
                msg += parsedMessage[i] + " ";
            }
            msg += parsedMessage[parsedMessage.length - 1];
            parsedMessage[2] = msg;
        }
        return parsedMessage;
    }

    /**
     * Determine if the whole command is valid.
     * A valid command is "!hm guess" or "!hm solve", etc.
     * @param message The message parsed into an array.
     * @return true if the command is valid, false otherwise.
     */
    private boolean validCommand(String[] message) {
        // Currently, valid messages will have a parsed length of 3:
        // [0] = initial command, [1] = sub command, [2] = guess
        if (message[0].equals("!hm")) {
            if (message[1].equalsIgnoreCase("guess") && message[2].length() == 1) {
                return true;
            }
            else if (message[1].equalsIgnoreCase("solve")) {
                return true;
            }
//            if (contains(message[1])) {
//                return message[1].equalsIgnoreCase("guess") && message[2].length() == 1;
//            }
//            else {
//                return message[1].equalsIgnoreCase("solve");
//            }
        }
        return false;
    }

    /**
     * Helper method to check if the parameter is in the list of commands.
     * @param message The part of the Twitch message containing the command.
     * @return true if the command is in the list of commands, false otherwise.
     */
    private boolean contains(String message) {
        for (String command : commands) {
            if (message.equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }
}
