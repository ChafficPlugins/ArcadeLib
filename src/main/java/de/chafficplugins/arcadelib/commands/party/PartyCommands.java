package de.chafficplugins.arcadelib.commands.party;

import de.chafficplugins.arcadelib.groups.Party;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.chafficplugins.arcadelib.localization.Localization.Key.*;
import static de.chafficplugins.arcadelib.localization.Localization.getLocalizedString;
import static de.chafficplugins.arcadelib.utils.ConfigStrings.PREFIX;

public class PartyCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("party")) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(PREFIX + getLocalizedString(COMMAND_NOT_A_PLAYER_ERROR));
                return true;
            }
            ArcadePlayer arcadePlayer = ArcadePlayer.getPlayer(player);
            if(arcadePlayer == null) {
                //TODO: Send error message
                return true;
            }
            if(args.length == 0) {
                if(arcadePlayer.party == null) {
                    arcadePlayer.sendMessage(PARTY_NOT_IN_A_PARTY);
                    return true;
                } else {
                    arcadePlayer.sendMessage(PARTY_CURRENT_MEMBERS);
                    for (ArcadePlayer member : arcadePlayer.party.players) {
                        arcadePlayer.sendMessage(member.getName());
                    }
                }
            } else if(args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null) {
                    arcadePlayer.sendMessage(PLAYER_NOT_FOUND);
                    return true;
                }
                ArcadePlayer arcadeTarget = ArcadePlayer.getPlayer(target);
                if(arcadeTarget == null) {
                    //TODO: Send error message
                    return true;
                }
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
