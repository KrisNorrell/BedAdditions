package me.bedpotato.bedadditions.events;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.utilities.menusystem.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    public MenuListener(BedAdditions e) {
        e.getServer().getPluginManager().registerEvents(this, e);
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof Menu menu) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            menu.handleMenu(e);
        }

    }

}
