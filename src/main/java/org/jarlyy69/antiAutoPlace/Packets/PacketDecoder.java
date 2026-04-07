package org.jarlyy69.antiAutoPlace.Packets;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.jarlyy69.antiAutoPlace.Main;
import org.jarlyy69.antiAutoPlace.User.User;

import java.util.EnumSet;
import java.util.Set;

/**
 * Class responsible for decoding incoming network packets.
 */
public class PacketDecoder extends ChannelDuplexHandler {
    /**
     * Set of interactable blocks
     */
    public static Set<Material> INTERACTABLE_BLOCKS = EnumSet.of(
            Material.DISPENSER,
            Material.NOTE_BLOCK,
            Material.CHEST,
            Material.WORKBENCH,
            Material.FURNACE,
            Material.BURNING_FURNACE,
            Material.LEVER,
            Material.STONE_BUTTON,
            Material.FENCE,
            Material.TRAP_DOOR,
            Material.FENCE_GATE,
            Material.NETHER_FENCE,
            Material.ENCHANTMENT_TABLE,
            Material.ENDER_CHEST,
            Material.BEACON,
            Material.WOOD_BUTTON,
            Material.ANVIL,
            Material.TRAPPED_CHEST,
            Material.DAYLIGHT_DETECTOR,
            Material.DAYLIGHT_DETECTOR_INVERTED,
            Material.HOPPER,
            Material.DROPPER,
            Material.IRON_TRAPDOOR,
            Material.SPRUCE_FENCE,
            Material.BIRCH_FENCE,
            Material.JUNGLE_FENCE,
            Material.DARK_OAK_FENCE,
            Material.ACACIA_FENCE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.SIGN_POST,
            Material.WALL_SIGN,
            Material.WOODEN_DOOR,
            Material.IRON_DOOR_BLOCK,
            Material.CAKE_BLOCK,
            Material.BED_BLOCK,
            Material.DIODE_BLOCK_ON,
            Material.DIODE_BLOCK_OFF,
            Material.BREWING_STAND,
            Material.CAULDRON,
            Material.ITEM_FRAME,
            Material.FLOWER_POT,
            Material.REDSTONE_COMPARATOR,
            Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.COMMAND,
            Material.VINE
    );

    public Player player;
    public CraftPlayer craftPlayer;
    public User user;

    public PacketDecoder(Player player) {
        this.player = player;
        this.craftPlayer = (CraftPlayer) player;
        this.user = Main.getInstance().getUserManager().users.get(player);
    }

    /**
     * Called when a packet is read from the channel
     *
     * @param ctx the channel handler context
     * @param msg the message (packet) received
     * @throws Exception if an error occurs during packet processing
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean shouldCancel = false;
        if (msg instanceof Packet) {
            shouldCancel = !handlePacket((Packet<?>) msg);
        }

        if (shouldCancel&&Main.getInstance().getConfigManager().cancel) {
            WorldServer server = ((CraftWorld)craftPlayer.getWorld()).getHandle();
            PacketPlayOutBlockChange change = new PacketPlayOutBlockChange(server,user.lastBlock);
            craftPlayer.getHandle().playerConnection.sendPacket(change);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.teleport(new Location(player.getWorld(),user.lastX,user.lastY,user.lastZ,user.lastYaw,user.lastPitch));
            });
            return;
        }

        super.channelRead(ctx, msg);
    }

    /**
     * Handles processing of different types of packets
     *
     * @param packet the packet to handle
     * @return true if the packet was handled successfully, otherwise false
     */
    public boolean handlePacket(Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            return handleBlockPlace((PacketPlayInBlockPlace) packet);
        }
        else if (packet instanceof PacketPlayInFlying) {
            return handleFlying((PacketPlayInFlying) packet);
        } else {
            return true;
        }
    }

    /**
     * Handles flying movement packets to update last position info and update tick counter
     *
     * @param packet the flying packet
     * @return true after updating
     */
    public boolean handleFlying(PacketPlayInFlying packet) {
        if (packet.f()) {
            user.lastX = packet.a();
            user.lastY = packet.b();
            user.lastZ = packet.c();
        }
        if (packet.g()) {
            user.lastYaw = packet.d();
            user.lastPitch = packet.e();
        }
        Main.getInstance().getUserManager().nextTick(player);
        return true;
    }

    /**
     * Processes block placement packets
     *
     * @param packet the block placement packet
     * @return true if processing was successful and player isn't using autoplace/fastplace, false otherwise
     */
    public boolean handleBlockPlace(PacketPlayInBlockPlace packet) {
        if (user.shouldBypass) return true;

        ItemStack itemStack = packet.getItemStack();
        if (itemStack == null || itemStack.getItem()==null) return true;

        Item item = itemStack.getItem();
        Material material = CraftMagicNumbers.getMaterial(item);
        if (material == Material.AIR) return true;
        if (!(item instanceof ItemBlock)) return true; // Not a block item
        if (item instanceof ItemStep) return true; // Ignore steps
        if (INTERACTABLE_BLOCKS.contains(material)&&!player.isSneaking()) return true;

        if (packet.getFace()==255) return true; // invalid face

        BlockPosition pos = packet.a();
        WorldServer server = ((CraftWorld)craftPlayer.getWorld()).getHandle();
        EnumDirection direction = EnumDirection.fromType1(packet.getFace());
        BlockPosition realPos = pos.shift(direction);

        if (user.lastBlock==realPos) return true;

        if (server.getType(realPos).getBlock()!=Blocks.AIR) return true;


        return !Main.getInstance().getUserManager().placeBlock(player,realPos);
    }
}
