package de.chafficplugins.arcadelib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ArcadeCommand {
    boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
