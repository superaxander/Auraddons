package alexanders.mods.auraddons.net;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.block.tile.TileAuraTransporter;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConnectionPacket implements IMessage {
    // HAHA THIS CODE IS SHIT
    private static final long NULL_VALUE = 0xFFFFFFEFFFFFFFFFL; // Corresponds to BlockPos(-1, -1025, -1) 

    private BlockPos pos;
    private BlockPos other;

    public ConnectionPacket() {}

    public ConnectionPacket(BlockPos pos, BlockPos other) {
        this.pos = pos;
        this.other = other;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        long val = buf.readLong();
        if (val != NULL_VALUE) {
            other = BlockPos.fromLong(val);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        if (other == null) {
            buf.writeLong(NULL_VALUE);
        } else {
            long val = other.toLong();
            buf.writeLong(val);
            if (val == NULL_VALUE) {
                Auraddons.logger.error("Mod Incompatibility?! Can't connect a transporter at block position -1, -1025, -1");
            }
        }
    }

    public static class Handler implements IMessageHandler<ConnectionPacket, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(ConnectionPacket message, MessageContext ctx) {
            Auraddons.proxy.runLater(() -> {
                World world = Minecraft.getMinecraft().world;
                if (world != null) {
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof TileAuraTransporter) {
                        ((TileAuraTransporter) te).other = message.other;
                    }
                }
            });
            return null;
        }
    }
}
