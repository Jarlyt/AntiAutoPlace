package org.jarlyy69.antiAutoPlace.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jarlyy69.antiAutoPlace.Main;

public class PlayerJoin implements Listener {
    /**
     * Injects a custom packet decoder on join
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Main.getInstance().getUserManager().addUser(player);
        Main.getInstance().getInjector().inject(player);
    }
}
