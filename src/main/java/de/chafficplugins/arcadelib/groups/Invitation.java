package de.chafficplugins.arcadelib.groups;

import de.chafficplugins.arcadelib.ArcadeLib;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import org.bukkit.Bukkit;

import static de.chafficplugins.arcadelib.utils.ConfigStrings.INVITATION_TIMEOUT;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0
 *
 * A class to handle invitations for groups.
 */
public class Invitation {
    /**
     * The player who send the invitation
     */
    public ArcadePlayer sender;
    /**
     * The player who receive the invitation
     */
    public ArcadePlayer receiver;
    /**
     * The group the player is invited to.
     */
    public Party group;

    /**
     * Default constructor that also adds a runnable that removes the invitation after a certain time.
     * @param sender The player who send the invitation
     * @param receiver The player who receive the invitation
     * @param group The group the player is invited to.
     */
    public Invitation(ArcadePlayer sender,
                      ArcadePlayer receiver,
                      Party group) {
        this.sender = sender;
        this.receiver = receiver;
        this.group = group;
        Bukkit.getScheduler().runTaskLater(ArcadeLib.getInstance(), () -> {
            Party.invitations.remove(this);
        }, INVITATION_TIMEOUT);
    }
}
