package org.jarlyy69.antiAutoPlace.Utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Manages plugin configuration options.
 */
public class ConfigManager {
    /**
     * Indicates whether PlaceholderAPI support is enabled.
     * Set to true if the plugin detects that PlaceholderAPI is installed,
     * allowing for placeholder replacements in messages
     */
    public boolean usePAPI = false;

    /** Whether to cancel the block placement event */
    public boolean cancel;

    /** Whether alerts are enabled */
    public boolean alert;

    /** The alert message to send when auto-place is detected */
    public String alertMessage;

    /** Whether punishment commands are enabled upon detection */
    public boolean punishment;

    /** The command to execute as punishment, e.g., banning or kicking the player */
    public String punishmentCommand;

    /** The plugin's configuration file. */
    public FileConfiguration config;

    /**
     * Constructor: loads configuration settings from the plugin's config file
     *
     * @param plugin the plugin instance
     */
    public ConfigManager(Plugin plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();

        cancel = config.getBoolean("cancel");

        alert = config.getBoolean("alerts.enabled");
        alertMessage = ChatColor.translateAlternateColorCodes('&',config.getString("alerts.message"));

        punishment = config.getBoolean("punishment.enabled");
        punishmentCommand = config.getString("punishment.command");
    }

    /**
     * Gets the alert message, parsing placeholders if using PAPI.
     *
     * @param player the player for placeholder replacement.
     * @return the formatted alert message.
     */
    public String getAlertMessage(Player player) {
        if (usePAPI) {
            return PlaceholderAPI.setPlaceholders(player, alertMessage);
        } else {
            return alertMessage.replace("%player%", player.getName());
        }
    }

    /**
     * Gets the punishment command, replacing the %player% placeholder.
     *
     * @param player the player for placeholder replacement.
     * @return the punishment command string.
     */
    public String getPunishmentCommand(Player player) {
        return punishmentCommand.replace("%player%", player.getName());
    }
}