package org.jarlyy69.antiAutoPlace.Api;

import org.bukkit.entity.Player;

/**
 * Interface for sending alerts to moderators when a cheater is detected
 */
public interface DetectAlerter {
    /**
     * Alerts moderators about the detected cheater
     *
     * @param player who uses cheats
     */
    void alert(Player player);
}
