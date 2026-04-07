package org.jarlyy69.antiAutoPlace.Packets;

import io.netty.channel.ChannelPipeline;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Class responsible for injecting a custom packet decoder
 */
public class PacketInjector {
    /**
     * Injects a custom packet decoder into the players network pipeline
     */
    public void inject(Player player) {
        PacketDecoder decoder = new PacketDecoder(player);
        getPipeline(player).addAfter("decoder","antiautoplace-decoder",decoder);
    }

    /**
     * Removes a custom packet decoder
     */
    public void uninject(Player player) {
        if (getPipeline(player).get("antiautoplace-decoder") != null) {
            getPipeline(player).remove("antiautoplace-decoder");
        };
    }

    /**
     * Retrieves the network channel pipeline for the specified player
     * @return player's network pipeline
     */
    public ChannelPipeline getPipeline(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
