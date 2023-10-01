package pt.gongas.quiz.user.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import pt.gongas.quiz.QuizPlugin;
import pt.gongas.quiz.category.impl.Football;
import pt.gongas.quiz.category.impl.Geography;
import pt.gongas.quiz.category.impl.History;
import pt.gongas.quiz.category.Category;
import pt.gongas.quiz.database.DatabaseConnector;
import pt.gongas.quiz.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserManager {

    private final List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    saveAll();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskTimerAsynchronously(QuizPlugin.getInstance(), 20 * 60 * 30, 20 * 60 * 30);
    }

    public void load() throws SQLException {

        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement("SELECT * FROM user");
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            JSONObject categories = (JSONObject) JSONValue.parse(result.getString("CATEGORIES"));
            new User(result.getString("NAME"), Arrays.asList(new Football(Math.toIntExact((Long) categories.get("Football"))), new Geography(Math.toIntExact((Long) categories.get("Geography"))), new History(Math.toIntExact((Long) categories.get("History")))));
        }

        statement.close();

        Bukkit.getConsoleSender().sendMessage("§a[devroom-quiz] §f" + this.users.size() + " §ausers were loaded");
    }

    public void saveAll() throws SQLException {
        for (User user : this.users)
            save(user, false);

        Bukkit.getConsoleSender().sendMessage("§a[devroom-quiz] §f" + this.users.size() + " §ausers were saved");
    }

    public void save(User user, boolean debug) throws SQLException {
        Map<String, Integer> obj = new HashMap<>();
        for (Category category : user.categories())
            obj.put(category.getIdentifier(), category.getScore());

        String categories = JSONValue.toJSONString(obj);

        PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement("INSERT INTO user (NAME, CATEGORIES) VALUES (?, ?) ON DUPLICATE KEY UPDATE CATEGORIES = ?");
        statement.setString(1, user.name());
        statement.setString(2, categories);

        statement.execute();
        statement.close();

        if (debug)
            Bukkit.getConsoleSender().sendMessage("§a[devroom-quiz] User §f" + user.name() + " §ahas been successfully saved.");
    }


    public boolean hasUser(String name) { return this.users.stream().anyMatch(user -> user.name().equalsIgnoreCase(name)); }

    public User loadUser(Player player) { return hasUser(player.getName()) ? getUser(player.getName()) : new User(player.getName(), Arrays.asList(new Football(0), new Geography(0), new History(0))); }

    public User getUser(String name) { return this.users.stream().filter(user -> user.name().equalsIgnoreCase(name)).findAny().orElse(null); }

    public void addUser(User user) {
        this.users.add(user);
    }

}
