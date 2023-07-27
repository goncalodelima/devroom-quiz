package pt.gongas.quiz.database;

import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MariaDB extends DatabaseConnector {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public MariaDB(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + database, username, password);
            Bukkit.getConsoleSender().sendMessage("§a[devroom-quiz] Connection opened successfully");
            createTable();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§c[devroom-quiz] Error while opening connection");
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("§a[devroom-quiz] Connection closed successfully");
            } catch (SQLException e) {
                System.out.println("§c[devroom-quiz] Error while closing connection");
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user (NAME VARCHAR(255), CATEGORIES TEXT)");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
