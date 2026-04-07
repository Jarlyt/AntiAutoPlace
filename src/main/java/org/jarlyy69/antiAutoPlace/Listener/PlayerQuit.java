package org.jarlyy69.antiAutoPlace.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jarlyy69.antiAutoPlace.Main;

public class PlayerQuit implements Listener {
    /**
     * Removes packet decoder on quit
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        Main.getInstance().getUserManager().removeUser(player);
        Main.getInstance().getInjector().uninject(player);
    }
}
