package de.chafficplugins.arcadelib.miniGames;

import de.chafficplugins.arcadelib.lobby.Lobby;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0
 *
 * MiniGame needs to be extended by every miniGame add on.
 * It stores all the information and logic for a miniGame.
 */
public abstract class MiniGame {
    /**
     * All registered miniGames.
     */
    private static final List<MiniGame> miniGames = new ArrayList<>();

    /**
     * Returns a random game.
     */
    public static MiniGame getRandomGame() {
        return miniGames.get((int) (Math.random() * miniGames.size()));
    }

    /**
     * All locations, players can spawn at
     */
    private List<Location> playerSpawns;
    /**
     * The spectator spawn location
     */
    private Location spectatorSpawn;
    /**
     * The position where all players will be teleported during waiting times
     */
    private Location lobbySpawn;
    /**
     * The area the arena is in.
     * Players will die if they leave this area during the game.
     * arenaArea[0] and arenaArea[1] span the arena cube.
     */
    private final Location[] arenaArea = new Location[2];
    /**
     * All current participants in the miniGame.
     */
    protected List<ArcadePlayer> participants = new ArrayList<>();
    /**
     * The lobby the miniGame is played in.
     */
    public Lobby lobby;

    /**
     * Default constructor to create and register a miniGame.
     * Needs to be called by every miniGame add on.
     */
    public MiniGame() {
        miniGames.add(this);
    }

    /**
     * Signals the miniGame to prepare for a start.
     * @param players The players that are going to play the miniGame.
     */
    public void prepare(ArcadePlayer[] players, Lobby lobby) {
        this.participants = List.of(players);
        this.lobby = lobby;
    }

    /**
     * Signals the miniGame it is time to start.
     */
    public abstract void start();

    /**
     * Finish the miniGame.
     */
    public abstract void finish();

    /**
     * Signals the miniGame to stop mid-game.
     * Under normal circumstances this should not be called.
     */
    public abstract void stop();

    public void arenaMessage(String message) {
        for (ArcadePlayer player : participants) {
            player.sendMessage(message);
        }
    }
}
