package alexanders.mods.auraddons.net;

import alexanders.mods.auraddons.Auraddons;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import io.netty.buffer.ByteBuf;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class ParticlePacket {
    private Type type = Type.SHOCK_WAVE;
    private BlockPos pos = BlockPos.ZERO;
    private BlockPos endPos = BlockPos.ZERO;
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

    public static ParticlePacket fromBytes(PacketBuffer buf) {
        ParticlePacket pkt = new ParticlePacket();
        pkt.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        pkt.type = Type.fromIndex(buf.readInt());
        if (pkt.type == Type.PARTICLE_STREAM) {
            pkt.endPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        }
        return pkt;
    }

    public static void toBytes(ParticlePacket pkt, ByteBuf buf) {
        buf.writeInt(pkt.pos.getX());
        buf.writeInt(pkt.pos.getY());
        buf.writeInt(pkt.pos.getZ());
        buf.writeInt(pkt.type.ordinal());
        if (pkt.type == Type.PARTICLE_STREAM) {
            buf.writeInt(pkt.endPos.getX());
            buf.writeInt(pkt.endPos.getY());
            buf.writeInt(pkt.endPos.getZ());

            buf.writeFloat(pkt.speed);
            buf.writeInt(pkt.color);
            buf.writeFloat(pkt.scale);
        }
    }

    public static void handleMessage(ParticlePacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
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
                        NaturesAuraAPI.instance().spawnParticleStream(message.pos.getX(), message.pos.getY(), message.pos.getZ(), message.endPos.getX(), message.endPos.getY(),
                                                                      message.endPos.getZ(), message.speed, message.color, message.scale);
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
}
