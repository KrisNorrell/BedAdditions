package me.bedpotato.bedadditions;

import me.bedpotato.bedadditions.events.InventoryClickEvent;
import me.bedpotato.bedadditions.manager.CommandManager;
import me.bedpotato.bedadditions.manager.addons.Addon;
import me.bedpotato.bedadditions.manager.addons.AddonLoader;
import me.bedpotato.bedadditions.utilities.InventoryUtil.Menu;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public final class BedAdditions extends JavaPlugin {
    private static BedAdditions plugin;
    public CommandManager manager;
    public File configFile;
    public FileConfiguration config = new YamlConfiguration();

    public static BedAdditions getPlugin() {
        return plugin;
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
        manager.setup();
        new InventoryClickEvent(this);
        new AddonLoader(this);
    }
    public void shutdown() {
        AddonLoader.getAddons().forEach(Addon::onShutdown);
        AddonLoader.getAddons().clear();
        HandlerList.unregisterAll(this);
        plugin.manager.commands.clear();
    }
}
