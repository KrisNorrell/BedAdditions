package me.bedpotato.bedadditions.manager.addons;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.utilities.FileUtil;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidPluginException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class AddonLoader {

    private final BedAdditions plugin;
    private static final List<Addon> addons = new CopyOnWriteArrayList<>();

    public AddonLoader(final BedAdditions plugin) {
        this.plugin = plugin;
        final File addonsFolder = new File(this.plugin.getDataFolder(), "/addons/");
        final File[] addonsInFolder = addonsFolder.listFiles();
        if (addonsInFolder == null) return;
        for (File file : addonsInFolder) {
            if (!file.getName().endsWith(".jar")) continue;
            try {
                final Addon addon = this.loadAddon(file);
                addons.add(addon);
                plugin.getLogger().info(ChatColor.GREEN + "Loaded addon: " + ChatColor.YELLOW + addon.getAddonName());
                addon.run();
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
        }
        plugin.getLogger().info(ChatColor.GREEN + "Finished loading " + addons.size() + " addon(s).");
    }
    public static List<Addon> getAddons() {
        return addons;
    }
    private Addon loadAddon(final File jarFile) throws InvalidPluginException {
        final String addonName = jarFile.getName().replace(".jar", "");
        Addon addon;
        try {
            Class<? extends Addon> mainClassFromJar = FileUtil.findClass(jarFile, Addon.class);
            final Class<? extends Addon> extendedClass = mainClassFromJar.asSubclass(Addon.class);

            addon = extendedClass.getDeclaredConstructor().newInstance();

        } catch (final IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
            throw new InvalidPluginException("Failed to initialize addon: " + jarFile.getName());
        }
        addon.setAddonName(addonName);
        addon.setAddonFile(jarFile);
        addon.setPlugin(this.plugin);
        return addon;
    }
}