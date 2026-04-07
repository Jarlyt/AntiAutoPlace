package org.jarlyy69.antiAutoPlace;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jarlyy69.antiAutoPlace.Api.AntiAPI;
import org.jarlyy69.antiAutoPlace.Listener.PlayerJoin;
import org.jarlyy69.antiAutoPlace.Listener.PlayerQuit;
import org.jarlyy69.antiAutoPlace.Packets.PacketInjector;
import org.jarlyy69.antiAutoPlace.User.UserManager;
import org.jarlyy69.antiAutoPlace.Utils.ConfigManager;

/**
 * Main class of the plugin
 */
public final class Main extends JavaPlugin {
    // ----- PLUGIN API -----
    private final AntiAPI api = new AntiAPI();

    // ----- MANAGERS -----
    private UserManager userManager = new UserManager();
    private PacketInjector injector;
    private ConfigManager configManager;

    /**
     * Gets the AntiAPI instance
     *
     * @return the AntiAPI object
     */
    public AntiAPI getApi() {
        return api;
    }

    /**
     * Instance of the plugin
     */
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this; // Save the plugin instance for static access

        // Initialize configuration manager
        configManager = new ConfigManager(this);
        // Check if PlaceholderAPI plugin is loaded; enable PAPI support if yes
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            configManager.usePAPI = true;
        }

        // Initialize user manager
        userManager = new UserManager();

        // Initialize packet injector (for autoplace detection)
        injector = new PacketInjector();

        // Register event listeners for player join and quit events
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
    }


    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * Gets the plugin instance
     *
     * @return the instance of Main
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Gets the UserManager instance
     *
     * @return the UserManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Gets the PacketInjector instance
     *
     * @return the PacketInjector
     */
    public PacketInjector getInjector() {
        return injector;
    }

    /**
     * Gets the ConfigManager instance
     *
     * @return the ConfigManager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
