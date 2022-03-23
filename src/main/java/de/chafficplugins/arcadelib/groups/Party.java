package de.chafficplugins.arcadelib.groups;

import de.chafficplugins.arcadelib.player.ArcadePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chaffic
 * @since 1.0.0
 *
 * A party is a collection of players who want to play together.
 * They join and leave lobbies together.
 */
public class Party {
    /**
     * The list of all players in this party.
     */
    public List<ArcadePlayer> players = new ArrayList<>();
    /**
     * The leader of the party.
     */
    private ArcadePlayer leader;
    /**
     * The maximum amount of players in this party. Defaults to 4.
     * Depends on permissions of the leader.
     */
    private int maxPartySize = 4;

    /**
     * Creates a party with the creator as leader and the given max party size.
     * @param player The creator of the party.
     * @param maxPartySize The maximum amount of players in this party.
     */
    public Party(ArcadePlayer player, int maxPartySize) {
        players.add(player);
        leader = player;
        this.maxPartySize = maxPartySize;
    }

    /**
     * Creates a party with the creator as leader.
     * @param player The creator of the party.
     */
    public Party(ArcadePlayer player) {
        players.add(player);
        leader = player;
    }

    /**
     * Tries to add a player to the party. Returns false if the party is already full.
     * @param player The player to add.
     * @return True if the player was added, false if the party is already full.
     */
    public boolean addPlayer(ArcadePlayer player) {
        if(players.size() < maxPartySize) {
            players.add(player);
            //TODO: Send message player joined party
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a player from the party. If the player was the leader, the second joined player becomes the leader.
     * The max party size will be updated.
     * @param player The player to remove.
     */
    public void removePlayer(ArcadePlayer player) {
        players.remove(player);
        //TODO: Send message player left party
        if(player == leader) {
            if(players.size() > 0) {
                //TODO: Send message new leader
                //TODO: change max party size
                leader = players.get(0);
            }
        }
    }

    /**
     * Sends a message to all players in the party.
     * @param message The message to send.
     */
    public void partyMessage(String message) {
        for(ArcadePlayer player : players) {
            player.sendMessage(message);
        }
    }

    /**
     * Returns the amount of players in the party.
     * @return The amount of players in the party.
     */
    public int size() {
        return players.size();
    }

    /**
     * Returns the leader of the party.
     * @return The leader of the party.
     */
    public ArcadePlayer getLeader() {
        return leader;
    }
}
