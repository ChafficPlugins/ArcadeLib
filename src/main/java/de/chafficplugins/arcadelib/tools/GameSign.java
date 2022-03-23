package de.chafficplugins.arcadelib.tools;

import de.chafficplugins.arcadelib.lobby.Lobby;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chaffic
 * @version 1.0
 * @since 1.0.0
 */
public class GameSign {
    /**
     * All registered GameSigns
     */
    private static final List<GameSign> signs = new ArrayList<>();

    /**
     * Gets the GameSign that belongs to the given sign.
     *
     * @param block The sign to get the GameSign from.
     * @return The GameSign that belongs to the given sign.
     */
    public static GameSign getSign(Block block) {
        for (GameSign gameSign : signs) {
            if (gameSign.block.equals(block)) {
                return gameSign;
            }
        }
        return null;
    }

    /**
     * The lobby this sign belongs to.
     */
    private Lobby lobby;
    /**
     * The block this class represents.
     */
    private Block block;

    /**
     * Creates a new GameSign and sets the text.
     * If there is already a GameSign for the given sign, this one will be altered to match the new settings.
     *
     * @param blockLocation The location of the sign.
     * @param lobby The lobby this sign belongs to.
     */
    public GameSign(Location blockLocation, Lobby lobby) {
        Block block = blockLocation.getBlock();
        GameSign gameSign = getSign(block);
        if (gameSign == null) {
            gameSign = this;
            signs.add(gameSign);
        }

        gameSign.lobby = lobby;

        gameSign.block = block;
        Sign sign = (Sign) block.getState();
        sign.setLine(0, ChatColor.AQUA + lobby.getName());
        sign.setLine(1, lobby.size() + " / " + lobby.maxPlayers);
        sign.setLine(2, ChatColor.YELLOW + "Loading...");
        sign.setLine(3, "");
        sign.update(true);
        block.getState().update(true);
        block.getWorld().setBlockData(block.getLocation(), sign.getBlockData()); //TODO Fix this!!!
    }

    /**
     * Handles the sign click.
     * Tries to join the lobby with the player or if party is not null with the party.
     *
     * @param player The player that clicked the sign.
     */
    public void onClick(ArcadePlayer player) {
        boolean isJoined;
        if (player.party != null) {
            if (player.party.getLeader() == player) {
                isJoined = lobby.playerJoin(player.party);
            } else {
                //TODO: Tell the player he is not the leader
                return;
            }
        } else {
            isJoined = lobby.playerJoin(player);
        }
        if (!isJoined) {
            //TODO: Tell the player the lobby is full
        }
    }

    /**
     * Sets line 2 to display waiting.
     */
    public void setLoading() {
        ((Sign)block.getState()).setLine(2, ChatColor.YELLOW + "Loading...");
        block.getState().update(true);
    }

    /**
     * Sets line 2 to display waiting.
     */
    public void setWaiting() {
        ((Sign)block.getState()).setLine(2, ChatColor.GREEN + "Waiting...");
        block.getState().update(true);
    }

    /**
     * Sets line 2 to display playing.
     */
    public void setPlaying() {
        ((Sign)block.getState()).setLine(2, ChatColor.GREEN + "Playing...");
        block.getState().update(true);
    }

    /**
     * Sets line 2 to display full.
     */
    public void setFull() {
        ((Sign)block.getState()).setLine(2, ChatColor.RED + "Full");
        block.getState().update(true);
    }

    /**
     * Updates line 1 to display the right amount of players.
     */
    public void updatePlayers() {
        ((Sign)block.getState()).setLine(1, lobby.size() + " / " + lobby.maxPlayers);
        block.getState().update(true);
    }
}
