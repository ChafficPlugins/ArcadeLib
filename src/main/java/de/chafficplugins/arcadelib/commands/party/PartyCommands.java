package de.chafficplugins.arcadelib.commands.party;

import de.chafficplugins.arcadelib.groups.Party;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("party")) {
            if(!(sender instanceof Player player)) {
                //TODO: Send message only player
                return true;
            }
            if(args.length == 0) {
                //TODO: Show party information
            } else if(args.length == 2) {
                ArcadePlayer arcadePlayer = ArcadePlayer.getPlayer(player);
                if(arcadePlayer == null) {
                    //TODO: Send error message
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null) {
                    //TODO: Send message player not found
                    return true;
                }
                ArcadePlayer arcadeTarget = ArcadePlayer.getPlayer(target);
                switch (args[0]) {
                    case "invite" -> {
                        Party.invite(arcadePlayer, arcadeTarget);
                        return true;
                    }
                    case "accept" -> {
                        Party.acceptInvitation(arcadePlayer, arcadeTarget);
                        return true;
                    }
                    case "decline" -> {
                        Party.declineInvitation(arcadePlayer, arcadeTarget);
                        return true;
                    }
                }
            } else {
                //TODO: Send message wrong arguments
                return true;
            }
        }
        return false;
    }
}
