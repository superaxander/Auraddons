package alexanders.mods.auraddons.net;

import alexanders.mods.auraddons.block.tile.TileAutoWrath;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class JumpPacket {
    private BlockPos pos = BlockPos.ZERO;
    private boolean jumping = false;

    public JumpPacket() {
    }

    public JumpPacket(BlockPos pos, boolean jumping) {
        this.pos = pos;
        this.jumping = jumping;
    }

    public static JumpPacket fromBytes(PacketBuffer buf) {
        JumpPacket pkt = new JumpPacket();
        pkt.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        pkt.jumping = buf.readBoolean();
        return pkt;
    }

    public static void toBytes(JumpPacket pkt, PacketBuffer buf) {
        buf.writeInt(pkt.pos.getX());
        buf.writeInt(pkt.pos.getY());
        buf.writeInt(pkt.pos.getZ());
        buf.writeBoolean(pkt.jumping);
    }

    public static void handleMessage(JumpPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity te = null;
            final ClientWorld world = Minecraft.getInstance().world;
            if (world != null) {
                te = world.getTileEntity(message.pos);
            }
            if (te instanceof TileAutoWrath) {
                ((TileAutoWrath) te).jumping = message.jumping;
                if (message.jumping) {
                    ((TileAutoWrath) te).startJumping();
                } else {
                    ((TileAutoWrath) te).stopJumping();

                }
            }
        });
    }
}
