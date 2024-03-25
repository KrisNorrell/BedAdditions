package me.bedpotato.bedadditions.manager;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface TabCompleteHandler {
    List<String> onTabComplete(CommandSender sender, String[] args);
}