package de.chafficplugins.arcadelib;

import de.chafficplugins.arcadelib.commands.ArcadeCommand;
import de.chafficplugins.arcadelib.commands.CommandListener;
import de.chafficplugins.arcadelib.commands.party.PartyCommands;
import de.chafficplugins.arcadelib.commands.setupCommands.LobbySetupCommands;
import de.chafficplugins.arcadelib.events.ArcadePlayerEvents;
import de.chafficplugins.arcadelib.events.GameSignEvents;
import de.chafficplugins.arcadelib.lobby.Lobby;
import de.chafficplugins.arcadelib.player.ArcadePlayer;
import de.chafficplugins.arcadelib.utils.Crucial;
import io.github.chafficui.CrucialAPI.Utils.Server;
import io.github.chafficui.CrucialAPI.Utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static de.chafficplugins.arcadelib.utils.ConfigStrings.*;

public final class ArcadeLib extends JavaPlugin {
    private static ArcadeLib instance;
    private final Logger logger = Logger.getLogger("ArcadeLib");

    public static ArcadeLib getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        Crucial.download();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            if (!Server.checkCompatibility("1.18", "1.17", "1.16", "1.15")) {
                error("Wrong server version. Please use a supported version.");
                error("This is NOT a bug. Do NOT report this!");
                throw new IOException();
            }

            if(Crucial.connect()) {
                instance = this;
                loadConfig();
                new Stats(this, BSTATS_ID);
                registerEvents(new GameSignEvents(), new ArcadePlayerEvents());
                registerCommand("arcadelib", new CommandListener());
                registerCommand("lobbysetup", new LobbySetupCommands());
                registerCommand("party", new PartyCommands());
                log(ChatColor.DARK_GREEN + getDescription().getName() + " is now enabled (Version: " + getDescription().getVersion() + ") made by "
                        + ChatColor.AQUA + getDescription().getAuthors() + ".");
                new Lobby("Lobby1", 4);
            }
        } catch (IOException e) {
            e.printStackTrace();
            error("Failed to startup " + getDescription().getName() + " (Version: " + getDescription().getVersion() + ")");
            getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getScheduler().cancelTasks(this);
        log(ChatColor.DARK_GREEN + getDescription().getName() + " is now disabled (Version: " + getDescription().getVersion() + ")");
    }

    /**
     * Registers an event listener to the plugin.
     * @param listeners The listener to register.
     */
    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(String name, ArcadeCommand command) {
        CommandListener.addCommand(name, command);
    }

    /**
     * Registers a command to the plugin.
     * @param name The name of the command.
     * @param executor The command executor class.
     */
    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor(executor);
        }
    }

    private void loadConfig() {
        getConfig().addDefault(START_KARMA_STRING, START_KARMA);
        getConfig().addDefault(LEAVE_WHILE_RUNNING_KARMA_PENALTY_STRING, LEAVE_WHILE_RUNNING_KARMA_PENALTY);
        getConfig().addDefault(STAYED_IN_GAME_KARMA_STRING, STAYED_IN_GAME_KARMA);
        getConfig().addDefault(TIME_BETWEEN_MINIGAMES_STRING, TIME_BETWEEN_MINIGAMES);
        getConfig().addDefault(INVITATION_TIMEOUT_STRING, INVITATION_TIMEOUT);
        getConfig().addDefault(START_GAME_DELAY_STRING, START_GAME_DELAY);
        getConfig().options().copyDefaults(true);
        saveConfig();

        START_KARMA = getConfigInt(START_KARMA_STRING);
        LEAVE_WHILE_RUNNING_KARMA_PENALTY = getConfigInt(LEAVE_WHILE_RUNNING_KARMA_PENALTY_STRING);
        STAYED_IN_GAME_KARMA = getConfigInt(STAYED_IN_GAME_KARMA_STRING);
        TIME_BETWEEN_MINIGAMES = getConfigInt(TIME_BETWEEN_MINIGAMES_STRING);
        INVITATION_TIMEOUT = getConfigInt(INVITATION_TIMEOUT_STRING);
        START_GAME_DELAY = getConfigInt(START_GAME_DELAY_STRING);
    }

    public boolean getConfigBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public int getConfigInt(String path) {
        return getConfig().getInt(path);
    }

    public String getConfigString(String path) {
        return getConfig().getString(path);
    }

    public void log(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.severe(message);
    }
}
