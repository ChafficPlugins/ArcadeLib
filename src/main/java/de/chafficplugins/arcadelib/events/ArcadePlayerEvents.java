package de.chafficplugins.arcadelib.events;

import de.chafficplugins.arcadelib.player.ArcadePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ArcadePlayerEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ArcadePlayer player = ArcadePlayer.getPlayer(event.getPlayer());
        if(player == null) {
            new ArcadePlayer(event.getPlayer());
        }
    }
}
