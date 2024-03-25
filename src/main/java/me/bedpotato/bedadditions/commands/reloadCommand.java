package me.bedpotato.bedadditions.commands;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.manager.SubCommand;
import me.bedpotato.bedadditions.manager.TabCompleteHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class reloadCommand extends SubCommand implements TabCompleteHandler {
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

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        completions.add("test 1");
        completions.add("test 2");
        completions.add("test 3");
        // Your tab completion logic for sub-command 1
        // Populate 'completions' based on the arguments 'args'
        return completions;
    }
}
