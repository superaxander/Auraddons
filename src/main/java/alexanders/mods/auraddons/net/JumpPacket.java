package alexanders.mods.auraddons.net;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.block.tile.TileAutoWrath;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class JumpPacket implements IMessage {
    private BlockPos pos = BlockPos.ORIGIN;
    private boolean jumping = false;

    public JumpPacket() {
    }

    public JumpPacket(BlockPos pos, boolean jumping) {
        this.pos = pos;
        this.jumping = jumping;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        jumping = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(jumping);
    }

    public static class Handler implements IMessageHandler<JumpPacket, IMessage> {
        @Override
        public IMessage onMessage(JumpPacket message, MessageContext ctx) {
            Auraddons.proxy.runLater(() -> {
                TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if (te instanceof TileAutoWrath) {
                    ((TileAutoWrath) te).jumping = message.jumping;
                    if (message.jumping) {
                        ((TileAutoWrath) te).startJumping();
                    } else {
                        ((TileAutoWrath) te).stopJumping();

                    }
                }
            });
            return null;
        }
    }
}
