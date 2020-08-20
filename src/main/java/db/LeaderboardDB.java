package db;

import java.sql.*;

public class LeaderboardDB implements Database {

    final String LEADERBOARD_URL = "jdbc:sqlite:" +
            "src/main/resources/leaderboard/leaderboard.db";

    Connection conn;

    /**
     * Connect to the leaderboard database. If the scores table hasn't been
     * created yet, it will be created.
     * @return
     */
    public Connection connect() {

        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
                    Statement stmt = conn.createStatement()) {
            String tableSql = "CREATE TABLE IF NOT EXISTS scores (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	username text,\n"
                    + "	correct_guesses integer,\n"
                    + " incorrect_guesses integer,\n"
                    + " games_won integer);";

            stmt.execute(tableSql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void executeSmple(String sql) {
        // Execute the statement.
        try {
            Statement s = conn.createStatement();
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username) {
        String sql = "SELECT username FROM scores\n"
                + " WHERE username = \"" + username + "\";";
        boolean exists = false;
        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            // If rs.next() is true, there was a result and the user exists.
            if (rs.next()) {
                exists = true;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    private void initializeNewPlayer(String username) {
        String sql = "INSERT INTO scores(" +
                "username,correct_guesses,incorrect_guesses,games_won)" +
                " VALUES(\"" + username + "\",0,0,0);";
        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCorrectGuesses(String username, int points) {
        String sql;
        // Add new player to database if they haven't played
        if (!userExists(username)) {
            initializeNewPlayer(username);
        }
        // UPDATE score.
        sql = "UPDATE scores"
                + " SET correct_guesses = correct_guesses + " + points
                + " WHERE username = \"" + username + "\";";

        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addInorrectGuesses(String username) {
        String sql;
        // Add new player to database if they haven't played
        if (!userExists(username)) {
            initializeNewPlayer(username);
        }
        // UPDATE score.
        sql = "UPDATE scores"
                + " SET incorrect_guesses = incorrect_guesses + " + 1
                + " WHERE username = \"" + username + "\";";

        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGamesWon(String username) {
        String sql;
        // Add new player to database if they haven't played
        if (!userExists(username)) {
            initializeNewPlayer(username);
        }
        // UPDATE score.
        sql = "UPDATE scores"
                + " SET games_won = games_won + " + 1
                + " WHERE username = \"" + username + "\";";

        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTopFiveGuesses() {
        String sql = "SELECT username, correct_guesses FROM scores"
                + " ORDER BY correct_guesses desc;";

        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            String userScores = "";
            ResultSet rs = stmt.executeQuery(sql);

            int loopControl = 1;
            int topX = 0;
            while (rs.next()) {
                if (rs.getString("username") != null) {
                    userScores += rs.getString("username") + ": ";
                    userScores += rs.getString("correct_guesses") + ", ";
                    topX++;
                }
                if (loopControl == 5) {
                    break;
                }
                loopControl++;

            }
            rs.close();

            if (topX == 0) {
                return "No players on the leaderboard.";
            }
            String output = "TOP " + topX + " CORRECT GUESSES: " + userScores;
            return output.substring(0, output.length() - 2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public int getIncorrectGuesses(String username) {
        int numIncorrectGuesses = 0;
        String sql = "SELECT incorrect_guesses FROM scores"
                + " WHERE username = \"" + username + "\";";
        try (Connection conn = DriverManager.getConnection(LEADERBOARD_URL);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                numIncorrectGuesses = rs.getInt("incorrect_guesses");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numIncorrectGuesses;
    }
}
