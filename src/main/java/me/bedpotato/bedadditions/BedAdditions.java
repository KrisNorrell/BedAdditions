package me.bedpotato.bedadditions;

import me.bedpotato.bedadditions.events.MenuListener;
import me.bedpotato.bedadditions.manager.CommandManager;
import me.bedpotato.bedadditions.manager.addons.Addon;
import me.bedpotato.bedadditions.manager.addons.AddonLoader;
import me.bedpotato.bedadditions.utilities.ConfigHandler;
import me.bedpotato.bedadditions.utilities.SQLUtil.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class BedAdditions extends JavaPlugin {
    private static BedAdditions plugin;
    public File configFile;
    public FileConfiguration config = new YamlConfiguration();

    public static BedAdditions getPlugin() {
        return plugin;
    }

    private static MySQL sql;
    public CommandManager manager;

    public MySQL getSql() {
        return sql;
    }


    @Override
    public void onEnable() {
        plugin = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                startup();
            }
        }.runTaskLater(this, 5);
    }

    @Override
    public void onDisable() {
        shutdown();
        getLogger().info(ChatColor.GREEN + "BedAdditions fully unloaded.");
    }

    private void connectToSQL() {
        sql = new MySQL(
                ConfigHandler.getType(),
                ConfigHandler.getHost(),
                ConfigHandler.getPort(),
                ConfigHandler.getDatabase(),
                ConfigHandler.getUsername(),
                ConfigHandler.getPassword()
        );
        try {
            sql.connect();
            getLogger().info("Connected to SQL database.");
        } catch (SQLException e) {
            getLogger().severe("Failed to connect to SQL database: " + e.getMessage());
            e.printStackTrace();
            // Set isSqlReady to false or handle the error appropriately
        }
    }

    public boolean isNullOrEmpty(ConfigurationSection section) {
        return section == null || section.getKeys(false).isEmpty();
    }

    public void startup() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }
        try {
            config.load(configFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        HandlerList.unregisterAll(this);
        manager = new CommandManager();

        if (!isNullOrEmpty(config.getConfigurationSection("storage"))) {
            // Check if the SQL configuration is present and valid
            connectToSQL(); // Only attempt to connect if SQL configuration is available
        } else {
            getLogger().warning("SQL configuration is missing or invalid. Database connection will not be established.");
        }

        manager.setup();
        new MenuListener(this);
        new AddonLoader(this);
    }


    public void shutdown() {
        AddonLoader.getAddons().forEach(Addon::onShutdown);
        AddonLoader.getAddons().clear();
        HandlerList.unregisterAll(this);
//        try {
//            sql.disconnect();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

}
