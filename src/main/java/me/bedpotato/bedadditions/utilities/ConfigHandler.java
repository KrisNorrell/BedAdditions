package me.bedpotato.bedadditions.utilities;

import me.bedpotato.bedadditions.BedAdditions;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigHandler {
    public static FileConfiguration getConfig() {
        return BedAdditions.getPlugin().config;
    }

    public static Object get(String path) {
        return getConfig().get(path);
    }

    public static List<String> getMessage(String path) {
        return getConfig().getStringList("messages." + path);
    }

    public static boolean isSQL() {
        return getConfig().getBoolean("mysql.use-mysql");
    }

    public static String getHost() {
        return getConfig().getString("mysql.host");
    }

    public static int getPort() {
        return getConfig().getInt("mysql.port");
    }

    public static String getDatabase() {
        return getConfig().getString("mysql.database");
    }

    public static String getUsername() {
        return getConfig().getString("mysql.username");
    }

    public static String getPassword() {
        return getConfig().getString("mysql.password");
    }

}
