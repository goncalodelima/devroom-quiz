package pt.gongas.twinscore.utils.database;

import org.bukkit.Bukkit;
import pt.gongas.twinscore.CorePlugin;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLite extends DatabaseConnector {

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + CorePlugin.getInstance().getDataFolder() + File.separator + "database.db");
            Bukkit.getConsoleSender().sendMessage("§a[twins-core] Connection opened successfully");
            createTables();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§c[twins-core] Error while opening connection");
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("§a[twins-core] Connection closed successfully");
            } catch (SQLException e) {
                System.out.println("§c[twins-core] Error while closing connection");
                e.printStackTrace();
            }
        }
    }

    public void createTables() {
        try {
            // Create cupboard table
            PreparedStatement cupboardStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS cupboard (NAME VARCHAR(255), LOCATION VARCHAR(255), LEVEL INT)");
            cupboardStatement.executeUpdate();
            cupboardStatement.close();

            // Create cupboardUser table
            PreparedStatement cupboardUserStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS cupboardUser (NAME VARCHAR(255), CUPBOARD VARCHAR(255))");
            cupboardUserStatement.executeUpdate();
            cupboardUserStatement.close();

            // Create gang table
            PreparedStatement gangStatemnet = connection.prepareStatement("CREATE TABLE IF NOT EXISTS gang (NAME VARCHAR(255), OWNER VARCHAR(255), MODERATORS TEXT, MEMBERS TEXT, POINTS INTEGER)");
            gangStatemnet.executeUpdate();
            gangStatemnet.close();

            // Create research table
            PreparedStatement researchStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS research (NAME VARCHAR(255), RESEARCHES TEXT, LEVELS INTEGER, POINTS INTEGER)");
            researchStatement.executeUpdate();
            researchStatement.close();

            // Create user table
            PreparedStatement userStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS user (NAME VARCHAR(255), DEATHLOCATION VARCHAR(255), KILLS INT, PLAYTIME FLOAT)");
            userStatement.executeUpdate();
            userStatement.close();

            // Create door table
            PreparedStatement doorStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS door (NAME VARCHAR(255), PLAYERS VARCHAR(255), LOCATION VARCHAR(255), PASSWORD INT)");
            doorStatement.executeUpdate();
            doorStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
