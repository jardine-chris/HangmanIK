package swing;

import bot.Client;
import bot.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Scanner;

/**
 * Create a GUI to allow the bot owner to start games of HangmanIK.
 *
 * @author Chris Jardine
 * @version 1.0
 */
public class LoginWindow {

    public static Client client;

    public static boolean isConnected;

    public static JFrame frame;
    private final JPanel panel;

    public static String nickname;
    public static String channel;
    public static String oauth;

    private Timer delayDisconnect;
    private Timer delayCustom;
    private Timer delayRandom;

    // COLORS
    private final Color mainBackground = Color.decode("#0e0e10");
    private final Color buttonGreen = Color.decode("#087f23");
    private final Color buttonRed = Color.decode("#b71c1c");
    private final Color buttonPurple = Color.decode("#9146ff");
    private final Color buttonGray = Color.decode("#1f1f23");
    private final Color textBoxGray = Color.decode("#18181b");

    // FONT
    public static Font montserrat;

    // BORDERS
    Border textBoxBorder = new LineBorder(buttonPurple, 1);
    Border textBoxInnerMargin = BorderFactory.createEmptyBorder(5, 3, 5, 3);

    /**
     * Default constructor.
     * @throws IOException
     */
    public LoginWindow() throws IOException {
        client = new Client();
        isConnected = false;

        // Change style of default elements. These are overridden as needed,
        // but work within JOptionPane. For more control, consider creating a
        // custom JDialog.
        UIManager.put("OptionPane.background", mainBackground);
        UIManager.put("Panel.background", mainBackground);
        UIManager.put("TextField.background", textBoxGray);
        UIManager.put("TextField.foreground", Color.white);
        UIManager.put("Button.background", buttonPurple);
        UIManager.put("Button.foreground", Color.white);

        // Load and register the custom font.
        try {
            montserrat = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/styles/Montserrat-Regular.ttf"));
            montserrat = montserrat.deriveFont(12f); // default size is 1
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(montserrat);
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        // Load the config file holding login data
        loadConfigFile();

        // Create and set up the UI frame and panel
        frame = new JFrame("HangmanIK Twitch Bot");
        frame.setSize(285, 210);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // open window centered on screen
        panel = new JPanel();
        panel.setBackground(mainBackground);
        frame.add(panel);
        frame.setResizable(false);

        // Change icon
        frame.setIconImage(ImageIO.read(new File("src/main/resources/styles/hangman-icon.png")));

        initializeGUI();
        frame.setVisible(true);
    }

    /**
     * When starting the program, this will load the config.txt file and initialize
     * the variables needed to make the connection.
     * @throws FileNotFoundException File wasn't found
     */
    private void loadConfigFile() throws FileNotFoundException {
        // Load the configuration file.
        Scanner s = new Scanner(new File("src/main/resources/config.txt"));
        String line;
        nickname = "";
        channel = ":#";
        oauth = "oauth:";

        while (s.hasNextLine()) {
            line = s.nextLine();
            if (!line.startsWith("//")) {
                if (line.contains("%")) {
                    String[] parsed = line.split("%");
                    if (parsed[0].equals("nickname") && parsed.length > 1) {
                        nickname = parsed[1];
                    } else if (parsed[0].equals("channel") && parsed.length > 1) {
                        channel = parsed[1];
                    } else if (parsed[0].equals("oauth") && parsed.length > 1) {
                        oauth = parsed[1];
                    }
                }
            }
        }
    }

    /**
     * Initialize the user interface. This creates the main window, places
     * components, and starts event and mouse listeners as needed.
     */
    private void initializeGUI() {
        panel.setLayout(null);

        // NICKNAME FIELD
        // label
        JLabel nameLabel = new JLabel("Bot Name");
        nameLabel.setBounds(10, 45, 80, 25);
        nameLabel.setFont(montserrat.deriveFont(Font.BOLD));
        nameLabel.setForeground(Color.white);
        panel.add(nameLabel);
        // text field
        JTextField nameText = new JTextField(nickname);
        nameText.setBounds(80, 45,  185, 25);
        nameText.setFont(montserrat);
        nameText.setForeground(Color.white);
        nameText.setBackground(textBoxGray);
        nameText.setBorder(BorderFactory.createCompoundBorder(textBoxBorder, textBoxInnerMargin));
        nameText.setCaretColor(Color.decode("#ffffff"));
        nameText.setEnabled(true);
        nameText.requestFocusInWindow();
        nameText.setCaretPosition(nameText.getText().length());
        panel.add(nameText);

        // CHANNEL FIELD
        // label
        JLabel channelLabel = new JLabel("Channel");
        channelLabel.setBounds(10, 75, 80, 25);
        channelLabel.setFont(montserrat.deriveFont(Font.BOLD));
        channelLabel.setForeground(Color.white);
        panel.add(channelLabel);
        // text field
        JTextField channelText = new JTextField(channel);
        channelText.setBounds(80, 75, 185, 25);
        channelText.setFont(montserrat);
        channelText.setForeground(Color.white);
        channelText.setBackground(textBoxGray);
        channelText.setBorder(BorderFactory.createCompoundBorder(textBoxBorder, textBoxInnerMargin));
        channelText.setCaretColor(Color.decode("#ffffff"));
        channelText.setEnabled(true);
        panel.add(channelText);

        // OAUTH FIELD
        // label
        JLabel oauthLabel = new JLabel("OAuth");
        oauthLabel.setBounds(10, 105, 80, 25);
        oauthLabel.setFont(montserrat.deriveFont(Font.BOLD));
        oauthLabel.setForeground(Color.white);
        panel.add(oauthLabel);
        // text field
        JTextField oauthText = new JTextField(oauth);
        oauthText.setBounds(80, 105, 185, 25);
        oauthText.setFont(montserrat);
        oauthText.setForeground(Color.white);
        oauthText.setBackground(textBoxGray);
        oauthText.setBorder(BorderFactory.createCompoundBorder(textBoxBorder, textBoxInnerMargin));
        oauthText.setCaretColor(Color.decode("#ffffff"));
        oauthText.setEnabled(true);
        panel.add(oauthText);

        // CONNECT BUTTON
        JButton connectButton = new JButton("Connect");
        connectButton.setBounds(10, 140, 255, 25);
        connectButton.setFont(montserrat.deriveFont(Font.BOLD));
        connectButton.setVisible(true);
        connectButton.setEnabled(true);
        connectButton.setBackground(buttonGreen);
        connectButton.setForeground(Color.white);
        connectButton.setBorder(null);
        connectButton.setFocusPainted(false);
        panel.add(connectButton);

        // DISCONNECT BUTTON
        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBounds(10, 140, 255, 25);
        disconnectButton.setFont(montserrat.deriveFont(Font.BOLD));
        disconnectButton.setVisible(false);
        disconnectButton.setEnabled(false);
        disconnectButton.setBackground(buttonRed);
        disconnectButton.setForeground(Color.white);
        disconnectButton.setBorder(null);
        disconnectButton.setFocusPainted(false);
        panel.add(disconnectButton);

        // CUSTOM WORD BUTTON
        JButton customWord = new JButton("Custom Word");
        customWord.setBounds(10, 10, 125, 25);
        customWord.setFont(montserrat.deriveFont(Font.BOLD));
        customWord.setVisible(true);
        customWord.setEnabled(false);
        customWord.setBackground(buttonGray);
        customWord.setForeground(Color.white);
        customWord.setBorder(BorderFactory.createCompoundBorder(textBoxBorder, textBoxInnerMargin));
        customWord.setFocusPainted(false);
        panel.add(customWord);

        // RANDOM WORD BUTTON
        JButton randomWord = new JButton("Random Word");
        randomWord.setBounds(140, 10, 125, 25);
        randomWord.setFont(montserrat.deriveFont(Font.BOLD));
        randomWord.setVisible(true);
        randomWord.setEnabled(false);
        randomWord.setBackground(buttonGray);
        randomWord.setForeground(Color.white);
        randomWord.setBorder(BorderFactory.createCompoundBorder(textBoxBorder, textBoxInnerMargin));
        randomWord.setFocusPainted(false);
        panel.add(randomWord);

        // Create MouseListener for connect button
        connectButton.addMouseListener(new MouseAdapter() {
            // On MouseOver
            @Override
            public void mouseEntered(MouseEvent e) {
                connectButton.setBackground(buttonGreen.brighter());
                connectButton.setBorder(null);
            }

            // On MouseOut
            @Override
            public void mouseExited(MouseEvent e) {
                connectButton.setBackground(buttonGreen);
            }
        });

        // Create MouseListener for disconnect button
        disconnectButton.addMouseListener(new MouseAdapter() {
            // On MouseOver
            @Override
            public void mouseEntered(MouseEvent e) {
                if (disconnectButton.isEnabled()) {
                    disconnectButton.setBackground(buttonRed.brighter());
                    disconnectButton.setBorder(null);
                }
            }

            // On MouseOut
            @Override
            public void mouseExited(MouseEvent e) {
                if (disconnectButton.isEnabled()) {
                    disconnectButton.setBackground(buttonRed);
                }
            }
        });

        // Create MouseListener for custom word button
        customWord.addMouseListener(new MouseAdapter() {
            // On MouseOver
            @Override
            public void mouseEntered(MouseEvent e) {
                if (customWord.isEnabled()) {
                    customWord.setBackground(buttonPurple);
                }
            }

            // On MouseOut
            @Override
            public void mouseExited(MouseEvent e) {
                if (customWord.isEnabled()) {
                    customWord.setBackground(buttonGray);
                }
            }
        });

        // Create MouseListener for random word button
        randomWord.addMouseListener(new MouseAdapter() {
            // On MouseOver
            @Override
            public void mouseEntered(MouseEvent e) {
                if (randomWord.isEnabled()) {
                    randomWord.setBackground(buttonPurple);
                }
            }

            // On MouseOut
            @Override
            public void mouseExited(MouseEvent e) {
                if (randomWord.isEnabled()) {
                    randomWord.setBackground(buttonGray);
                }
            }
        });

        // Create ActionListener for connect button
        connectButton.addActionListener(e -> {
            // Set variables for logging in and authenticating.
            nickname = nameText.getText();
            channel = channelText.getText();
            oauth = oauthText.getText();

            // Start the client on a new thread
            Runnable r = () -> {
                client.connect();
            };
            new Thread(r).start();

            // Update the UI to prevent changes to the text boxes and
            // to change the connect button to disconnect
            nameText.setEnabled(false);
            channelText.setEnabled(false);
            oauthText.setEnabled(false);
            connectButton.setVisible(false);
            disconnectButton.setVisible(true);
            disconnectButton.setEnabled(false);
            // Start timers before enabling buttons, allowing server time to connect
            delayDisconnect.start();
            delayCustom.start();
            delayRandom.start();
        });

        // Create action listener for disconnect button
        disconnectButton.addActionListener(actionEvent -> {
            nameText.setEnabled(true);
            channelText.setEnabled(true);
            oauthText.setEnabled(true);
            connectButton.setVisible(true);
            disconnectButton.setVisible(false);
            disconnectButton.setEnabled(false);
            connectButton.setVisible(true);
            connectButton.setEnabled(true);
            customWord.setEnabled(false);
            randomWord.setEnabled(false);
            nameText.requestFocusInWindow();
            client.disconnect();
            isConnected = false;
        });

        // ActionListener for Custom Word button
        customWord.addActionListener(actionEvent -> {
            Game newGame = new Game(client);
            newGame.chooseWord();
        });

        // ActionListener for Random Word button
        randomWord.addActionListener(actionEvent -> {
            Game newGame = new Game(client);
            newGame.randomWord();
        });

        // Create new Timer objects
        // Timer to delay the disconnect button active state
        int DELAY_SECS = 3;
        delayDisconnect = new Timer(DELAY_SECS * 1000, new DelayListener(disconnectButton));
        delayCustom = new Timer(DELAY_SECS * 1000, new DelayListener(customWord));
        delayRandom = new Timer(DELAY_SECS * 1000, new DelayListener(randomWord));
        delayDisconnect.setRepeats(false);
        delayCustom.setRepeats(false);
        delayRandom.setRepeats(false);
    }

    static class DelayListener implements ActionListener {
        JComponent target;

        public DelayListener(JComponent target) {
            this.target = target;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            target.setEnabled(true);
        }
    }
}