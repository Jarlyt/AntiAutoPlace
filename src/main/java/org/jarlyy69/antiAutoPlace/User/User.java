package org.jarlyy69.antiAutoPlace.User;

import net.minecraft.server.v1_8_R3.BlockPosition;
import org.bukkit.entity.Player;
import org.jarlyy69.antiAutoPlace.Utils.Permissions;

public class User {
    public Player player;
    public int tick; // Current client tick
    public int blocks; // Number of blocks placed in the current tick.
    public int flags; // Number of times the player has been flagged by the plugin for autoplace.
    public boolean shouldBypass; // Indicates whether the user should bypass block placement checks
    public BlockPosition lastBlock; // Last block position placed by the user

    // ---- LAST KNOWN LOCATION ----
    public double lastX;
    public double lastY;
    public double lastZ;
    public float lastYaw;
    public float lastPitch;

    public User(Player player) {
        this.player = player;
        this.shouldBypass = player.hasPermission(Permissions.BYPASS);
        this.tick = 0;
        this.blocks = 0;
        this.flags = 0;
    }

    /**
     * Called every game tick to update tick and reset block count.
     */
    public void tick() {
        this.tick++;
        this.blocks = 0;
    }

    /**
     * Records block placement and updates lastBlock position.
     *
     * @param block the BlockPosition of the placed block
     */
    public void block(BlockPosition block) {
        this.blocks++;
        this.lastBlock = block;
    }
}
