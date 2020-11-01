package alexanders.mods.auraddons.net;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.block.tile.TileAuraTransporter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ConnectionPacket {
    private static final long NULL_VALUE = 0xFFFFFFEFFFFFFFFFL; // Corresponds to BlockPos(-1, -1025, -1) 

    private BlockPos pos;
    private BlockPos other;

    public ConnectionPacket() {
    }

    public ConnectionPacket(BlockPos pos, BlockPos other) {
        this.pos = pos;
        this.other = other;
    }

    public static ConnectionPacket fromBytes(PacketBuffer buf) {
        ConnectionPacket pkt = new ConnectionPacket();
        pkt.pos = BlockPos.fromLong(buf.readLong());
        long val = buf.readLong();
        if (val != NULL_VALUE) {
            pkt.other = BlockPos.fromLong(val);
        }
        return pkt;
    }

    public static void toBytes(ConnectionPacket pkt, PacketBuffer buf) {
        buf.writeLong(pkt.pos.toLong());
        if (pkt.other == null) {
            buf.writeLong(NULL_VALUE);
        } else {
            long val = pkt.other.toLong();
            buf.writeLong(val);
            if (val == NULL_VALUE) {
                Auraddons.logger.error(
                        "Mod Incompatibility?! Can't connect a transporter at block position -1, -1025, -1");
            }
        }
    }

    @SuppressWarnings("Convert2Lambda")
    //@OnlyIn(Dist.CLIENT)
    public static void handleMessage(ConnectionPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                World world = Minecraft.getInstance().world;
                if (world != null) {
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof TileAuraTransporter) {
                        ((TileAuraTransporter) te).other = message.other;
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
