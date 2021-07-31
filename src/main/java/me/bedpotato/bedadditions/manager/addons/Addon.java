package me.bedpotato.bedadditions.manager.addons;

import me.bedpotato.bedadditions.BedAdditions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class Addon {

    private BedAdditions plugin;
    private String addonName;
    private String addonVersion;
    private String addonDescription;
    private File configFile;
    private File thisAddonFile;
    private boolean isEnabled = false;
    private YamlConfiguration config;
    private ClassLoader classLoader;

    public Addon() {
        this.classLoader = this.getClass().getClassLoader();
    }

    /**
     * Sets the config files
     */
    public void init() {
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public abstract void run();
    /**
     * Gets the JavaPlugin of the Core class
     * @return JavaPlugin
     */
    public BedAdditions getPlugin() {
        return this.plugin;
    }

    public void setPlugin(BedAdditions plugin) {
        this.plugin = plugin;
    }



    /**
     * Gets the data folder of the specified Addon
     * @return File of the data folder
     */
    public File getDataFolder() {
        return new File(this.getPlugin().getDataFolder(), "/addons/" + this.addonName);
    }

    /**
     * Saves the default config.yml in a addons jar file
     */
    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            saveResource("config.yml", true);
            this.configFile = new File(this.getDataFolder(), "config.yml");
            this.config = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    /**
     * Returns the YamlConfiguration for gthe file
     * @return YamlConfiguration of the default config
     */
    public YamlConfiguration getConfig() {
        return this.config;
    }

    /**
     * Reloads the configuration file (config.yml)
     */
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    /**
     * Saves the configuration file (config.yml)
     */
    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets a file content stream inside an addons jar
     * @param fileName File name to get
     * @return Embedded File's InputStream
     */
    public InputStream getResource(String fileName) {
        if (fileName == null) throw new IllegalArgumentException("File name cannot be null");
        try {
            ZipFile zipFile = new ZipFile(this.thisAddonFile.getAbsolutePath());
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            if (zipEntry == null) throw new RuntimeException("Embedded resource for Addon: " + this.addonName +
                    " Does not exist (" + fileName + ")");
            return zipFile.getInputStream(zipEntry);
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Saves a resource embedded into an addons jar file
     * @param pathToFile Path to the file to save
     * @param shouldReplace Should this file be replaced if it already exists?
     */
    public void saveResource(String pathToFile, boolean shouldReplace) {
        pathToFile = pathToFile.replace('\\', '/');
        InputStream inStream = getResource(pathToFile);
        if (inStream == null) {
            System.out.println(ChatColor.RED + "The resource " + pathToFile + " for addon " + this.addonName + " could not be found");
            return;
        }
        File out = new File(plugin.getDataFolder(), pathToFile);


        if (!out.exists() || shouldReplace) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            BufferedWriter writer;
            String line;
            try {
                writer = new BufferedWriter(new FileWriter(out));
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
                reader.close();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println(ChatColor.RED + "Could not save file " + out.getName() + " for addon " + this.addonName
                    + " Because it already exists");
        }
    }
    public void copyConfig() {
        FileConfiguration toCopy = new YamlConfiguration();
        try {
            toCopy.load(new InputStreamReader(getResource("config.yml")));
            for (String config : toCopy.getKeys(false)) {
                if (!plugin.config.contains(config)) {
                    plugin.config.set(config, toCopy.get(config));
                }
                plugin.config.save(plugin.configFile);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    /**
     * This is not to be used by Addon developers.
     * @param name Addon name to set
     */
    public void setAddonName(String name) {
        this.addonName = name;
    }

    /**
     * Returns the name of this addon (Can be used by Addon developers)
     * @return Name of this addon
     */
    public String getAddonName() {
        return  this.addonName;
    }

    /**
     * Sets the version of this addon, not to be used by addon developers
     * @param version Addon version to set
     */
    public void setAddonVersion(String version) {
        this.addonVersion = version;
    }

    /**
     * Gets the version of this addon (Can be used by addon developers)
     * @return Version of this addon
     */
    public String getAddonVersion() {
        return this.addonVersion;
    }

    /**
     * Sets the description of this addon, not to be used by addon developers
     * @param description Description of this adodn to set
     */
    public void setAddonDescription(String description) { this.addonDescription = description; }
    /**
     * Gets the description of this addon (Can be used by addon developers)
     * @return Description of this addon
     */
    public String getAddonDescription() {
        return this.addonDescription;
    }
    /**
     * Sets the addon file so we can use embedded resources
     * @param file Addon File
     */
    public void setAddonFile(File file) { this.thisAddonFile = file; }

    /**
     * Returns this addon file
     * @return This addons file
     */
    public File getAddonFile() { return this.thisAddonFile; }

}