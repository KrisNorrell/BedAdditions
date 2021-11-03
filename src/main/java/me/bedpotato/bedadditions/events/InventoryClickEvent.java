package me.bedpotato.bedadditions.events;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.utilities.InventoryUtil.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickEvent implements Listener {
    public InventoryClickEvent(BedAdditions main) {
        main.getServer().getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    public void onClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            Menu menu = (Menu) holder;
            menu.handleMenu(event);
        }
    }
}
