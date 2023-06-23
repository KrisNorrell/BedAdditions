package me.bedpotato.bedadditions.commands;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.manager.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class reloadCommand extends SubCommand {
    private final BedAdditions plugin = BedAdditions.getPlugin();
    @Override
    public void onCommand(CommandSender player, String[] args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[&2BedAdditions&3] &eReloading..."));
        plugin.shutdown();
        plugin.startup();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3[&2BedAdditions&3] &aSuccessfully reloaded all files!"));
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public String permission() {
        return "ba.reload";
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public boolean gameOnly() {
        return false;
    }
}
