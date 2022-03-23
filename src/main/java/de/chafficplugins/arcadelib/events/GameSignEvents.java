package de.chafficplugins.arcadelib.events;

import de.chafficplugins.arcadelib.lobby.Lobby;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import de.chafficplugins.arcadelib.tools.GameSign;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static de.chafficplugins.arcadelib.utils.ConfigStrings.CREATE_SIGN_LINE_1;
import static de.chafficplugins.arcadelib.utils.ConfigStrings.CREATE_SIGN_LINE_2;

public class GameSignEvents implements Listener {

    /**
     * Called when the player places a sing.
     * Registers if the sign is a lobby join sign.
     * These must be build like this:
     * 0: CREATE_SIGN_LINE_1
     * 1: CREATE_SIGN_LINE_2
     * 2: lobby name
     * @param event the event
     */
    @EventHandler
    public void onSignCreated(SignChangeEvent event) {
        String[] lines = event.getLines();
        //TODO: Permissions
        if(lines[0].equalsIgnoreCase(CREATE_SIGN_LINE_1) && lines[1].equalsIgnoreCase(CREATE_SIGN_LINE_2)) {
            Lobby lobby = Lobby.getLobby(lines[2]);
            if(lobby != null) {
                lobby.signs.add(new GameSign(event.getBlock().getLocation(), lobby));
            } else {
                //TODO: Tell player that there is no lobby with that name.
            }
        }
    }

    /**
     * Called when the player clicks a join sign.
     * @param event the event
     */
    @EventHandler
    public void onSignClicked(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        if(block.getState() instanceof Sign) {
            GameSign gameSign = GameSign.getSign(block);
            ArcadePlayer player = ArcadePlayer.getPlayer(event.getPlayer());
            if(gameSign != null && player != null) {
                gameSign.onClick(player);
            }
        }
    }
}
