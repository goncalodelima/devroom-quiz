package pt.gongas.quiz.database;

import org.bukkit.Bukkit;
import pt.gongas.quiz.QuizPlugin;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite extends DatabaseConnector {

    private final String database;

    public SQLite(String database) {
        this.database = database;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + QuizPlugin.getInstance().getDataFolder() + File.separator + this.database);
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
