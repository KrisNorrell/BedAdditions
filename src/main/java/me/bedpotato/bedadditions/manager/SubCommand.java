package me.bedpotato.bedadditions.manager;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    public SubCommand() {}

    public void onCommand(CommandSender sender, String[] args) {}

    public abstract String name();

    public abstract String[] aliases();

    public abstract String permission();

    public abstract String usage();
    public abstract boolean gameOnly();
}
