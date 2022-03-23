package de.chafficplugins.arcadelib;

import de.chafficplugins.arcadelib.commands.ArcadeCommand;
import de.chafficplugins.arcadelib.commands.CommandListener;
import de.chafficplugins.arcadelib.commands.setupCommands.LobbySetupCommands;
import de.chafficplugins.arcadelib.events.ArcadePlayerEvents;
import de.chafficplugins.arcadelib.events.GameSignEvents;
import de.chafficplugins.arcadelib.lobby.Lobby;
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
import java.util.logging.Logger;

import static de.chafficplugins.arcadelib.utils.ConfigStrings.BSTATS_ID;

public final class ArcadeLib extends JavaPlugin {
    private final Logger logger = Logger.getLogger("ArcadeLib");

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
                loadConfig();
                new Stats(this, BSTATS_ID);
                registerEvents(new GameSignEvents(), new ArcadePlayerEvents());
                registerCommand("arcadelib", new CommandListener());
                registerCommand("lobbysetup", new LobbySetupCommands());
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
        //getConfig().addDefault(AUTO_UPDATE, true);
        getConfig().options().copyDefaults(true);
        saveConfig();
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
