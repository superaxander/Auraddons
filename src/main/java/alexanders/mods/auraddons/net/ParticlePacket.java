package alexanders.mods.auraddons.net;

import alexanders.mods.auraddons.Auraddons;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ParticlePacket implements IMessage {
    private Type type = Type.SHOCK_WAVE;
    private BlockPos pos = BlockPos.ORIGIN;
    private BlockPos endPos = BlockPos.ORIGIN;
    private float speed;
    private int color;
    private float scale;

    public ParticlePacket(Type type, BlockPos pos) {
        this.type = type;
        this.pos = pos;
    }

    public ParticlePacket(BlockPos startPos, BlockPos endPos, float speed, int color, float scale) {
        this.type = Type.PARTICLE_STREAM;
        this.pos = startPos;
        this.endPos = endPos;
        this.speed = speed;
        this.color = color;
        this.scale = scale;
    }

    public ParticlePacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        type = Type.fromIndex(buf.readInt());
        if (type == Type.PARTICLE_STREAM) {
            endPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(type.ordinal());
        if (type == Type.PARTICLE_STREAM) {
            buf.writeInt(endPos.getX());
            buf.writeInt(endPos.getY());
            buf.writeInt(endPos.getZ());

            buf.writeFloat(speed);
            buf.writeInt(color);
            buf.writeFloat(scale);
        }
    }

    public enum Type {
        SHOCK_WAVE, PARTICLE_STREAM, FREEZE;

        public static Type fromIndex(int index) {
            if (index < 0 || index >= values().length) {
                Auraddons.logger.error("Received invalid particle index: {}", index);
                return values()[0];
            }
            return values()[index];
        }
    }

    public static class Handler implements IMessageHandler<ParticlePacket, IMessage> {
        @Override
        public IMessage onMessage(ParticlePacket message, MessageContext ctx) {
            Auraddons.proxy.runLater(() -> {
                World world = Minecraft.getMinecraft().world;
                if (world != null) {
                    switch (message.type) {
                        case SHOCK_WAVE:  // Shockwave
                            for (int i = 0; i < 360; i += 2) {
                                double rad = Math.toRadians(i);
                                NaturesAuraAPI.instance()
                                        .spawnMagicParticle(message.pos.getX() + .5f, message.pos.getY() + 0.01F, message.pos.getZ() + .5f, (float) Math.sin(rad) * 0.65F, 0F,
                                                            (float) Math.cos(rad) * 0.65F, 0x911b07, 3F, 10, 0F, false, true);
                            }
                            break;
                        case PARTICLE_STREAM:
                            break;
                        case FREEZE:
                            for (int i = world.rand.nextInt(20) + 20; i >= 0; i--) {
                                boolean side = world.rand.nextBoolean();
                                float x = side ? world.rand.nextFloat() : (world.rand.nextBoolean() ? 1.1F : -0.1F);
                                float z = !side ? world.rand.nextFloat() : (world.rand.nextBoolean() ? 1.1F : -0.1F);
                                NaturesAuraAPI.instance()
                                        .spawnMagicParticle(message.pos.getX() + x, message.pos.getY() + 0.1F + world.rand.nextFloat() * 0.98F, message.pos.getZ() + z, 0F, 0F, 0F,
                                                            0xBCE3FF, world.rand.nextFloat() + 1F, 50, 0F, true, true);
                            }
                    }
                }
            });
            return null;
        }
    }
}
