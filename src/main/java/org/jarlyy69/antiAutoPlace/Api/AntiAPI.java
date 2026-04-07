package org.jarlyy69.antiAutoPlace.Api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jarlyy69.antiAutoPlace.Main;
import org.jarlyy69.antiAutoPlace.Utils.Permissions;

public class AntiAPI {
    /**
     * The alerter responsible for notifying moderators about the violator
     */
    private DetectAlerter alerter;

    /**
     * Constructor: sets the default alerter implementation,
     * which sends a message to all players with ALERTS permission or operators
     */
    public AntiAPI() {
        alerter = player -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission(Permissions.ALERTS)||p.isOp()) {
                    p.sendMessage(Main.getInstance().getConfigManager().getAlertMessage(player));
                }
            }
        };
    }

    /**
     * Sets a custom alerter, allowing different notification implementations
     *
     * @param alerter the new DetectAlerter instance
     */
    public void setAlerter(DetectAlerter alerter) {
        this.alerter = alerter;
    }

    /**
     * Retrieves the current DetectAlerter
     *
     * @return the active DetectAlerter
     */
    public DetectAlerter getAlerter() {
        return this.alerter;
    }
}
