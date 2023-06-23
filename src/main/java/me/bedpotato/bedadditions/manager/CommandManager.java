package me.bedpotato.bedadditions.manager;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.commands.reloadCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    public final ArrayList<SubCommand> commands = new ArrayList<>();
    private final BedAdditions plugin = BedAdditions.getPlugin();

    public CommandManager() {
        registerCmd(new reloadCommand());
    }
    public void registerCmd(SubCommand cmd) {
        this.commands.add(cmd);
    }
    public String main = "ba";

    public void setup() {
        plugin.getCommand(main).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase(main)) {
                if (args.length == 0) {
                    if (cmdList(sender).size() > 0) {
                        sender.sendMessage(ChatColor.GREEN + "Commands:");
                        for (SubCommand sc : commands) {
                            if (sc.permission() == null || sender.hasPermission(sc.permission())) {
                                sender.sendMessage("/" + main + " " + (sc.usage() == null ? sc.name() : sc.usage()));
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Insufficient Permissions!");
                    }
                    return true;
                }

                SubCommand target = this.get(sender, args[0]);
                if (target == null) {
                    return true;
                }
                ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(args));
                arrayList.remove(0);
                String[] arguments = arrayList.toArray(new String[0]);

                try {
                    target.onCommand(sender, arguments);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "An error has occurred, please contact a staff member.");
                    e.printStackTrace();
                }
            }
        return true;
    }

    private SubCommand get(CommandSender player, String name) {
        for (SubCommand sc : this.commands) {
            if (sc.name().equalsIgnoreCase(name)) {
                if (sc.permission() == null || player.hasPermission(sc.permission())) {
                    if (sc.gameOnly() && (player instanceof ConsoleCommandSender)) {
                       player.sendMessage(ChatColor.RED + "Command may only be ran in game!");
                       return null;
                    }
                    return sc;
                } else {
                    player.sendMessage(ChatColor.DARK_RED + "Insufficient Permissions!");
                    return null;
                }
            }

            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    if (sc.permission() == null || player.hasPermission(sc.permission())) {
                        return sc;
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "Insufficient Permissions!");
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            List<String> all = cmdList(sender);
            if (args.length >= 1 && all.size() > args.length)
            {
                int index = args.length-1;
                return all.stream().filter(s -> args[index].isEmpty() || s.startsWith(args[index].toLowerCase())).sorted().toList();
            }
        }
        return null;
    }

    public List<String> cmdList(CommandSender player) {
        List<String> list = new ArrayList<>();
        for (SubCommand string : this.commands) {
            if (string.permission() == null || player.hasPermission(string.permission())) {
                list.add(string.name());
                list.addAll(Arrays.asList(string.aliases()));
            }
        }
        return list;
    }
}
