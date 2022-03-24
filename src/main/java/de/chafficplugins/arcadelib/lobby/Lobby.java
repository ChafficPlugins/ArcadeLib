package de.chafficplugins.arcadelib.lobby;

import de.chafficplugins.arcadelib.ArcadeLib;
import de.chafficplugins.arcadelib.miniGames.MiniGame;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import de.chafficplugins.arcadelib.groups.Party;
import de.chafficplugins.arcadelib.tools.GameSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

import static de.chafficplugins.arcadelib.utils.ConfigStrings.*;

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
     * The minimum amount of players in this lobby
     */
    public final int minPlayers;
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
    private final Set<ArcadePlayer> players = new HashSet<>();

    /**
     * Default constructor to create a lobby instance.
     * minPlayers will be set to 2
     * @param maxPlayers The maximum amount of players in this lobby
     */
    public Lobby(String name, int maxPlayers) {
        this.name = name;
        if(lobbies.contains(this)) {
            throw new IllegalArgumentException("Lobby already exists");
        }
        this.maxPlayers = maxPlayers;
        this.minPlayers = 2;
        lobbies.add(this);
    }

    /**
     * Default constructor to create a lobby instance.
     * @param maxPlayers The maximum amount of players in this lobby
     * @param minPlayers The minimum amount of players in this lobby
     */
    public Lobby(String name, int maxPlayers, int minPlayers) {
        this.name = name;
        if(lobbies.contains(this)) {
            throw new IllegalArgumentException("Lobby already exists");
        }
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
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
        updatePlayerAmount();
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
        updatePlayerAmount();
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
        updatePlayerAmount();
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
        updatePlayerAmount();
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
     * Teleports everyone to the lobby spawn.
     */
    public void teleportToLobbySpawn() {
        for (ArcadePlayer arcadePlayer : players) {
            arcadePlayer.teleport(lobbySpawn);
        }
    }

    /**
     * Teleports everyone to the leave location.
     */
    public void teleportToLeaveLocation() {
        for (ArcadePlayer arcadePlayer : players) {
            arcadePlayer.teleport(leaveLocation);
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

    //Lobby lifecycle
    private BukkitTask gameStartTimer; //TODO: Display timer in lobby

    /**
     * Updates the player amount at signs and the scoreboard.
     * Also checks if the game can start, or has to stop.
     */
    private void updatePlayerAmount() {
        for (GameSign gameSign : signs) {
            gameSign.updatePlayers();
        }

        //TODO: UPDATE SCOREBOARD

        if(players.size() >= minPlayers && currentGame == null) {
            startGameStartTimer(START_GAME_DELAY);
        } else if(currentGame != null && players.size() < minPlayers) {
            currentGame.stop();
        } else if(gameStartTimer != null && players.size() < minPlayers) {
            gameStartTimer.cancel();
        }
    }

    /**
     * Starts the game start timer.
     */
    private void startGameStartTimer(long time) {
        gameStartTimer = Bukkit.getScheduler().runTaskLater(ArcadeLib.getInstance(), () -> {
            if(players.size() >= minPlayers) {
                startGame();
            }
        }, time);
    }

    /**
     * Starts a random game.
     */
    private void startGame() {
        currentGame = MiniGame.getRandomGame();
        currentGame.lobby = this;
        currentGame.start();
    }

    /**
     * Called by the currentGame, when it is ready. Should never be called manually.
     * Teleports everyone to the game spawn, assigns points and starts a new game.
     */
    public void finishGame(HashMap<ArcadePlayer, Integer> points) {
        currentGame = null;
        //TODO: Assign points
        teleportToLobbySpawn();
        startGameStartTimer(TIME_BETWEEN_MINIGAMES);
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Lobby) {
            return ((Lobby) obj).name.equals(name);
        }
        return false;
    }
}
