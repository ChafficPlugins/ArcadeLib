package de.chafficplugins.arcadelib.player;

import de.chafficplugins.arcadelib.groups.Party;
import de.chafficplugins.arcadelib.localization.Localization;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static de.chafficplugins.arcadelib.localization.Localization.getLocalizedString;
import static de.chafficplugins.arcadelib.utils.ConfigStrings.START_KARMA;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0
 *
 * A registered player with features the arcade game needs.
 */
public class ArcadePlayer {
    /**
     * All players that are registered on the server.
     */
    private static final List<ArcadePlayer> arcadePlayers = new ArrayList<>();

    /**
     * The karma amount a specific player has.
     */
    private int karma = START_KARMA;
    /**
     * The party a player is in. Null if the player is not in a party.
     */
    public Party party;
    /**
     * The players bukkit UUID.
     */
    private final UUID uuid;

    /**
     * Creates a new ArcadePlayer if the player is not already registered.
     * @param player The player to create the ArcadePlayer for.
     * @throws IllegalArgumentException If the player is already registered.
     */
    public ArcadePlayer(Player player) {
        this.uuid = player.getUniqueId();
        if(!arcadePlayers.contains(this)) {
            arcadePlayers.add(this);
        } else {
            throw new IllegalArgumentException("Player already exists");
        }
    }

    /**
     * Gets the bukkit player.
     * @return The bukkit player.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Gets the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return getPlayer().getName();
    }

    /**
     * Gets the ArcadePlayer for the given player.
     * @param player The player to get the ArcadePlayer for.
     * @return The ArcadePlayer for the given player. Null if the player is not registered.
     */
    public static ArcadePlayer getPlayer(Player player) {
        for (ArcadePlayer arcadePlayer : arcadePlayers) {
            if(arcadePlayer.uuid.equals(player.getUniqueId())) {
                return arcadePlayer;
            }
        }
        return null;
    }

    /**
     * Teleports the player to the given location.
     * @param location The location to teleport the player to.
     */
    public void teleport(Location location) {
        getPlayer().teleport(location);
    }

    /**
     * Sends a chat message to the player.
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        getPlayer().sendMessage(message);
    }

    /**
     * Sends a chat localized message to the player.
     * @param key The message to send.
     */
    public void sendMessage(Localization.Key key) {
        sendMessage(getLocalizedString(key));
    }

    /**
     * Sends a chat localized message with placeholders to the player.
     * @param key The message to send.
     */
    public void sendMessage(Localization.Key key, String... placeholders) {
        sendMessage(getLocalizedString(key, placeholders));
    }

    public void removeKarma(int karma) {
        if(this.karma <= karma) this.karma = 0;
        else this.karma-=karma;
        //TODO: Tell player he lost karma
    }

    public void addKarma(int karma) {
        this.karma += karma;
    }

    /**
     * Tries to join the given party.
     * If the player is already in a party, he gets a message.
     * If the party is full, he gets a message.
     * @param party The party to join.
     */
    public void joinParty(Party party) {
        if(this.party != null) {
            //TODO: Send message that you are already in a party
        } else {
            if(party.addPlayer(this)) {
                this.party = party;
            } else {
                //TODO: Send message that party is full
            }
        }
    }

    /**
     * Leaves the party he is in.
     */
    public void leaveParty() {
        if (this.party != null) {
            this.party.removePlayer(this);
            this.party = null;
        } else {
            //TODO: Send message that you are not in a party
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArcadePlayer that = (ArcadePlayer) o;
        return Objects.equals(uuid, that.uuid);
    }
}
