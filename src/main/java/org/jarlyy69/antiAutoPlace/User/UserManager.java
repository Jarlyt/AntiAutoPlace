package org.jarlyy69.antiAutoPlace.User;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jarlyy69.antiAutoPlace.Main;
import org.jarlyy69.antiAutoPlace.Utils.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    public Map<Player,User> users = new HashMap<>();

    /**
     * Adds a new user to the manager
     *
     * @param player the player to add
     */
    public void addUser(Player player) {
        users.put(player, new User(player));
    }

    public void removeUser(Player player) {
        users.remove(player);
    }

    /**
     * Updates the user's state each tick
     *
     * @param player the player whose status to update
     */
    public void nextTick(Player player) {
        User user = users.get(player);
        if (user != null) {
            user.tick(); // Update the tick timer for the user
        }
    }

    /**
     * Handles when a user attempts to place a block
     *
     * @param player the player placing the block
     * @param pos    the position of the block
     * @return true if the player placed 2 or more blocks in a single game tick,
     *         which indicates the use of auto-place or fast-place. Returns false otherwise
     */
    public boolean placeBlock(Player player, BlockPosition pos) {
        User user = users.get(player);

        if (user == null) return false;

        user.block(pos);

        if (user.blocks>=2) {
            user.flags++;

            ConfigManager manager = Main.getInstance().getConfigManager();

            if (manager.punishment && user.flags>2) {
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(),manager.getPunishmentCommand(player));
                });
            }

            if (manager.alert) Main.getInstance().getApi().getAlerter().alert(player);

            return true;
        }
        return false;
    }
}
