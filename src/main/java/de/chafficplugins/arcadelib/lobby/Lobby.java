package de.chafficplugins.arcadelib.lobby;

import de.chafficplugins.arcadelib.miniGames.MiniGame;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import de.chafficplugins.arcadelib.groups.Party;
import de.chafficplugins.arcadelib.tools.GameSign;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

import static de.chafficplugins.arcadelib.utils.ConfigStrings.LEAVE_WHILE_RUNNING_KARMA_PENALTY;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0
 *
 * Represents a lobby, players can join to take part in mini-games.
 */
public class Lobby {
    /**
     * List of all available lobbies.
     */
    public static List<Lobby> lobbies = new ArrayList<>();

    public static Lobby getLobby(String name) {
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equals(name)) {
                return lobby;
            }
        }
        return null;
    }

    /**
     * All signs that lead to joining the lobby.
     */
    public final List<GameSign> signs = new ArrayList<>();
    /**
     * The unique name of a lobby.
     */
    private final String name;
    /**
     * The currently played game in this lobby
     */
    private MiniGame currentGame;
    /**
     * The maximum amount of players in this lobby
     */
    public final int maxPlayers;
    /**
     * The location each player will be teleported when the lobby is waiting for players or
     * when a player joins while a game is running
     */
    public Location lobbySpawn;
    /**
     * The location a player will be teleported to when he leaves the lobby
     */
    public Location leaveLocation;
    /**
     * All players that are currently in the lobby
     */
    private final List<ArcadePlayer> players = new ArrayList<>();

    /**
     * Default constructor to create a lobby instance.
     * @param maxPlayers The maximum amount of players in this lobby
     */
    public Lobby(String name, int maxPlayers) {
        this.name = name;
        if(lobbies.contains(this)) {
            throw new IllegalArgumentException("Lobby already exists");
        }
        this.maxPlayers = maxPlayers;
        lobbies.add(this);
    }

    /**
     * Adds a player to the lobby and teleports him to the lobby spawn.
     * @param arcadePlayer The player who wants to join the lobby
     * @return True if the player could be added to the lobby, false if the lobby is full
     */
    public boolean playerJoin(ArcadePlayer arcadePlayer) {
        if (players.size()+1 > maxPlayers) {
            return false;
        }
        players.add(arcadePlayer);
        arcadePlayer.teleport(lobbySpawn);
        //TODO: Send message, that player joined lobby
        updateSignPlayerNumber();
        return true;
    }

    /**
     * Adds all players of a party to the lobby and teleports them to the lobby spawn.
     * @param party The party who wants to join the lobby
     * @return True if the party could be added to the lobby, false if the lobby is full
     */
    public boolean playerJoin(Party party) {
        if (players.size()+party.size() > maxPlayers) {
            return false;
        }
        players.addAll(party.players);
        for (ArcadePlayer arcadePlayer : party.players) {
            arcadePlayer.teleport(lobbySpawn);
            //TODO: Send message, that player joined lobby
        }
        updateSignPlayerNumber();
        return true;
    }

    /**
     * Removes a player from the lobby and teleports him to the leave location.
     * Also removes karma if a game was running.
     * @param arcadePlayer The player who wants to leave the lobby
     */
    public void playerLeave(ArcadePlayer arcadePlayer) {
        players.remove(arcadePlayer);
        if(currentGame != null) {
            arcadePlayer.removeKarma(LEAVE_WHILE_RUNNING_KARMA_PENALTY);
        }
        updateSignPlayerNumber();
        arcadePlayer.teleport(leaveLocation);
    }

    /**
     * Removes all players of a party from the lobby and teleports them to the leave location.
     * Also removes karma from the leader if a game was running.
     * @param party The party who wants to leave the lobby
     */
    public void playerLeave(Party party) {
        players.removeAll(party.players);
        party.getLeader().removeKarma(LEAVE_WHILE_RUNNING_KARMA_PENALTY);
        for (ArcadePlayer arcadePlayer : party.players) {
            arcadePlayer.teleport(leaveLocation);
        }
        updateSignPlayerNumber();
    }

    /**
     * Sends a message to all players in the lobby.
     * @param message The message to send.
     */
    public void lobbyMessage(String message) {
        for (ArcadePlayer arcadePlayer : players) {
            arcadePlayer.sendMessage(message);
        }
    }

    /**
     * Returns the unique name of the lobby.
     * @return The unique name of the lobby
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the amount of players in the lobby.
     * @return The amount of players in the lobby
     */
    public int size() {
        return maxPlayers;
    }

    /**
     * Updates the player number on each sign.
     */
    private void updateSignPlayerNumber() {
        for (GameSign gameSign : signs) {
            gameSign.updatePlayers();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Lobby) {
            return ((Lobby) obj).name.equals(name);
        }
        return false;
    }
}
