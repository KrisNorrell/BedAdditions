package me.bedpotato.bedadditions.utilities.InventoryUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 45;
    protected int index = 0;

    public PaginatedMenu(Player player) {
        super(player);
    }
    public void addMenuBorder(){
        inventory.setItem(48, makeItem(Material.ARROW, ChatColor.GREEN + "Previous"));
        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));
        inventory.setItem(50, makeItem(Material.ARROW, ChatColor.GREEN + "Next"));
        for (int i = 45; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}
