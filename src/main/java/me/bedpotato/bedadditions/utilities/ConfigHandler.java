package me.bedpotato.bedadditions.utilities;

import me.bedpotato.bedadditions.BedAdditions;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
    public static FileConfiguration getConfig() {
        return BedAdditions.getPlugin().config;
    }

    public static String getType() {
        return getConfig().getString("storage.type");
    }

    public static String getHost() {
        return getConfig().getString("storage.host");
    }

    public static int getPort() {
        return getConfig().getInt("storage.port");
    }

    public static String getDatabase() {
        return getConfig().getString("storage.database");
    }

    public static String getUsername() {
        return getConfig().getString("storage.username");
    }

    public static String getPassword() {
        return getConfig().getString("storage.password");
    }

}

