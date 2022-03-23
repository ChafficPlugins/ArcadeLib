package de.chafficplugins.arcadelib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CommandListener implements CommandExecutor {
    private static final HashMap<String, ArcadeCommand> arcadeCommands = new HashMap<>();

    public static void addCommand(String command, ArcadeCommand listener) {
        arcadeCommands.put(command, listener);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("arcadelib")) {
            if(args.length == 0) return false;
            if(arcadeCommands.containsKey(args[0].toLowerCase())) {
                return arcadeCommands.get(args[0].toLowerCase()).onCommand(sender, command, label, args);
            }
            return false;
        }
        return false;
    }
}
