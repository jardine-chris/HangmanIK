package bot;

import com.gikk.twirk.Twirk;
import com.gikk.twirk.TwirkBuilder;
import com.gikk.twirk.events.TwirkListener;
import swing.LoginWindow;

import java.io.IOException;

/**
 * Representation of the bot connecting to Twitch's IRC.
 * The Twitch integration makes use of Gikkman's Twirk library:
 * https://github.com/Gikkman/Java-Twirk
 *
 * @author Chris Jardine
 * @version 1.0
 */
public class Client {

    Twirk client;

    private boolean isConnected;

    /**
     * Default constructor initializes <code>isConnected</code> as
     * <code>false</code>.
     */
    public Client() {
        isConnected = false;
    }

    /**
     * Connect the client to the Twitch server and initialize the
     * <i>IrcListeners</i> to listen to commands.
     */
    public void connect() {
        try {
            // Build the client object
            // The login info is taken from the text fields on the GUI.
            client = new TwirkBuilder(LoginWindow.channel, LoginWindow.nickname, LoginWindow.oauth)
                    .setVerboseMode(true)    // Log messages to/from Twitch
                    .build();

            // Add IrcListeners.
            client.addIrcListener(new Commands(client));
            client.addIrcListener(getOnDisconnectListener(client));

            client.connect();    //Connect to Twitch
            isConnected = true;

        } catch (IOException | InterruptedException e) {
            System.out.println(e + "\nUnable to connect to host: " + e.getMessage());
        }
    }

    /**
     * Disconnect the client from Twitch servers.
     */
    public void disconnect() {
        client.disconnect();
    }

    /**
     * Get the client Twirk object.
     * @return The client Twirk object.
     */
    public Twirk getClient() {
        return client;
    }

    /**
     * Implements the <code>getOnDisconnectListener</code> method of the
     * <code>TwirkListener</code> interface.
     * @param twirk The client object.
     * @return The TwirkListener.
     */
    private static TwirkListener getOnDisconnectListener(final Twirk twirk) {

        return new TwirkListener() {
            @Override
            public void onDisconnect() {
                // Twitch might sometimes disconnects us from chat. If so, try to reconnect.
                try {
                    if (!twirk.connect())
                        // Reconnecting might fail, for some reason. If so, close the connection and release resources.
                        twirk.close();
                } catch (IOException e) {
                    // If reconnection threw an IO exception, close the connection and release resources.
                    twirk.close();
                } catch (InterruptedException e) {
                    System.out.println("Unable to connect: " + e.getMessage());
                }
            }
        };
    }
}
