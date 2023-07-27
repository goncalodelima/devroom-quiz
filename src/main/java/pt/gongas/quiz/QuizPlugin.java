package pt.gongas.quiz;

import lombok.Getter;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pt.gongas.quiz.category.interfaces.Category;
import pt.gongas.quiz.command.QuizCommand;
import pt.gongas.quiz.database.DatabaseConnector;
import pt.gongas.quiz.database.MariaDB;
import pt.gongas.quiz.database.SQLite;
import pt.gongas.quiz.inventory.QuizInventory;
import pt.gongas.quiz.listener.PlayerListener;
import pt.gongas.quiz.user.User;
import pt.gongas.quiz.user.manager.UserManager;
import pt.gongas.quiz.utils.Configuration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public final class QuizPlugin extends JavaPlugin {

    @Getter
    public HashMap<User, Category> quiz = new HashMap<>();
    @Getter
    private ViewFrame viewFrame;
    @Getter
    private UserManager userManager;
    @Getter
    private Configuration categories;
    @Getter
    private Configuration lang;
    @Getter
    private Configuration inventories;
    @Getter
    private DatabaseConnector dataBase;
    @Getter
    public static QuizPlugin instance;

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        if (getConfig().getBoolean("MariaDB.enable")) dataBase = new MariaDB(getConfig().getString("MariaDB.hostname"), getConfig().getInt("MariaDB.port"), getConfig().getString("MariaDB.database"), getConfig().getString("MariaDB.username"), getConfig().getString("MariaDB.password"));
        else dataBase = new SQLite("database.db");

        dataBase.connect();

        categories = new Configuration(this, "categories", "categories.yml");
        categories.saveDefaultConfig();

        lang = new Configuration(this, "lang", "lang.yml");
        lang.saveDefaultConfig();

        inventories = new Configuration(this, "inventories", "inventories.yml");
        inventories.saveDefaultConfig();

        userManager = new UserManager();
        try {
            userManager.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        viewFrame = ViewFrame.of(this, new QuizInventory());
        viewFrame.register();

        register();

    }

    @Override
    public void onDisable() {
        try {
            userManager.saveAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataBase.close();
    }

    public void register() {
        registerCommand();
        registerListener();
    }

    public void registerCommand() { Objects.requireNonNull(Bukkit.getPluginCommand("quiz")).setExecutor(new QuizCommand()); }

    public void registerListener() { Bukkit.getPluginManager().registerEvents(new PlayerListener(), this); }


}
