package pt.gongas.quiz;

import lombok.Getter;
import lombok.SneakyThrows;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BossPlugin extends JavaPlugin {

    @Getter
    private ViewFrame viewFrame;
    @Getter
    private UserManager user;
    private final int port = getServer().getPort();
    public static BossPlugin instance;

    @SneakyThrows
    @Override
    public void onEnable() {

        new BukkitRunnable() {
            @SneakyThrows
            @Override
            public void run() {
                if (!checkValueReturned()) Bukkit.getPluginManager().disablePlugin(BossPlugin.this);
            }
        }.runTaskTimerAsynchronously(this, 20 * 60 * 60, 20 * 60 * 60);

        if (checkValueReturned()) {
            instance = this;
            saveDefaultConfig();
            if (getConfig().getBoolean("MySQL.use"))
                dataBase = new MySQL(getConfig().getString("MySQL.host"), getConfig().getString("MySQL.database"), getConfig().getString("MySQL.username"), getConfig().getString("MySQL.password"), getConfig().getInt("MySQL.port"));
            else
                dataBase = new SQLite();

            boss = new Configuration(this, "boss", "config.yml");
            boss.saveDefaultConfig();

            bossLang = new Configuration(this, "boss", "lang.yml");
            bossLang.saveDefaultConfig();

            inventories = new Configuration(this, "boss", "inventories.yml");
            inventories.saveDefaultConfig();

            enchantment = new Configuration(this, "enchantment", "config.yml");
            enchantment.saveDefaultConfig();

            enchantManager = new EnchantManager();
            enchantManager.load();
            bossDisplayManager = new BossDisplayManager();
            bossDisplayManager.loadBossDisplay();
            bossManager = new BossManager();
            bossManager.load();
            userManager = new UserManager();
            userManager.load();
            userManager.runTaskTimerAsynchronously(this, 0, 20 * 60 * 5);

            viewFrame = ViewFrame.of(this, new BossGeneralInventory(), new BossRewardInventory(), new BossKillerInventory(), new BossTopInventory());
            viewFrame.register();

            getCommand("boss").setExecutor(new BossCommand());
            getCommand("killer").setExecutor(new KillerCommand());
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

            new BukkitRunnable() {
                @Override
                public void run() {
                    userManager.saveAll();
                }
            }.runTaskTimerAsynchronously(this, 20 * 60 * 30, 20 * 60 * 30);
        } else {
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                getConfig().options().copyDefaults(true);
                saveDefaultConfig();
            }

            getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        userManager.saveAll();
        bossManager.saveAll();
        List<Boss> bosses = new ArrayList<>(bossManager.getBosses());
        bosses.forEach(boss -> boss.delete(false));
        dataBase.close();
    }

    public static BossPlugin getInstance() {
        return instance;
    }

    private boolean checkValueReturned() throws IOException {
        String value;

        URL checkip = new URL("http://checkip.amazonaws.com/%22");
        BufferedReader in = new BufferedReader(new InputStreamReader(checkip.openStream()));

        String ip = in.readLine();
        String url = "http://51.222.142.4/admin/api.php?pl=5859831e2b3db23528c710b1451e13fc&port=" + port + "&ip=" + ip + "&key=" + getConfig().getString("license");

        URLConnection connection = new URL(url).openConnection();
        BufferedReader leitor = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        value = leitor.readLine();
        leitor.close();

        return Objects.equals(value, "true" + ip);
    }

}
