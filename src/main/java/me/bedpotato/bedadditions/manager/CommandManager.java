package me.bedpotato.bedadditions.manager;

import me.bedpotato.bedadditions.BedAdditions;
import me.bedpotato.bedadditions.commands.reloadCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

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

    private final Map<CommandSender, List<String>> completedArguments = new HashMap<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        // Check if the sender is a player
        if (sender instanceof Player) {
            List<String> all = cmdList(sender);

            // Check if the sender has completed arguments for this sub-command
            List<String> completedArgs = completedArguments.getOrDefault(sender, new ArrayList<>());
            String currentArg = args[args.length - 1].toLowerCase();

            // If the current argument has been completed, return an empty list
            if (completedArgs.contains(currentArg)) {
                return completions;
            }

            if (args.length == 1) {
                // Filter and copy partial matches to completions list for the main command
                StringUtil.copyPartialMatches(args[args.length - 1], all, completions);
            } else {
                String subCommand = args[0].toLowerCase();
                SubCommand sc = get(sender, subCommand);

                if (sc instanceof TabCompleteHandler) {
                    // Invoke tab completion logic for the sub-command
                    completions = ((TabCompleteHandler) sc).onTabComplete(sender, args);
                    completedArgs.add(currentArg); // Mark the current argument as completed
                    completedArguments.put(sender, completedArgs);
                }
            }
        }

        return completions;
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

    public void setTabComplete(int position, List<String> list) {

    }
}
