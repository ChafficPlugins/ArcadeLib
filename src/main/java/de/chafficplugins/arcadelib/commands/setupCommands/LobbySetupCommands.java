package de.chafficplugins.arcadelib.commands.setupCommands;

import de.chafficplugins.arcadelib.commands.ArcadeCommand;
import de.chafficplugins.arcadelib.lobby.Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbySetupCommands implements ArcadeCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //TODO: Permissions
        if(!(sender instanceof Player player)) {
            //TODO: Send only player message
            return true;
        }
        if (args.length <= 1) return true;
        switch (args[1].toLowerCase()) {
            case "setlobby" -> {
                if(args.length == 3) {
                    Lobby lobby = Lobby.getLobby(args[2]);
                    if(lobby == null) {
                        //TODO: Send not a lobby message
                    } else {
                        lobby.lobbySpawn = player.getLocation();
                        System.out.println("Lobby " + lobby.getName() + " set to " + lobby.lobbySpawn.toString());
                    }
                } else {
                    //TODO: Send advice message
                }
            }
            case "setleave" -> {
                if(args.length == 3) {
                    Lobby lobby = Lobby.getLobby(args[2]);
                    if(lobby == null) {
                        //TODO: Send not a lobby message
                    } else {
                        lobby.leaveLocation = player.getLocation();
                    }
                } else {
                    //TODO: Send advice message
                }
            }
            case "create" -> {
                if(args.length == 4 || args.length == 5) {
                    Lobby lobby = Lobby.getLobby(args[2]);
                    if(lobby != null) {
                        //TODO: Send already exists message
                    } else {
                        int maxPlayers;
                        int minPlayers = 2;
                        try {
                            maxPlayers = Integer.parseInt(args[3]);
                            if(args.length == 5) {
                                minPlayers = Integer.parseInt(args[4]);
                            }
                        } catch (NumberFormatException e) {
                            //TODO: Send not a number message
                            return true;
                        }
                        new Lobby(args[2], maxPlayers, minPlayers);
                    }
                }
            }
        }
        return true;
    }
}
