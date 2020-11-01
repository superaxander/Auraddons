package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.net.ConnectionPacket;
import alexanders.mods.auraddons.net.JumpPacket;
import alexanders.mods.auraddons.net.ParticlePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPackets {
    private static final String VERSION = "1";
    private static SimpleChannel net;

    public static void init() {
        net = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.MOD_ID, "network"), () -> VERSION,
                VERSION::equals, VERSION::equals);
        //        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
        net.registerMessage(0, ParticlePacket.class, ParticlePacket::toBytes, ParticlePacket::fromBytes,
                ParticlePacket::handleMessage);
        net.registerMessage(1, JumpPacket.class, JumpPacket::toBytes, JumpPacket::fromBytes, JumpPacket::handleMessage);
        net.registerMessage(2, ConnectionPacket.class, ConnectionPacket::toBytes, ConnectionPacket::fromBytes,
                ConnectionPacket::handleMessage);
        //        });
        //        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
        //            net.registerMessage(0, ParticlePacket.class, ParticlePacket::toBytes, ParticlePacket::fromBytes, null);
        //            net.registerMessage(1, JumpPacket.class, JumpPacket::toBytes, JumpPacket::fromBytes, null);
        //            net.registerMessage(2, ConnectionPacket.class, ConnectionPacket::toBytes, ConnectionPacket::fromBytes, null);
        //        });
    }

    public static <MSG> void sendAround(World world, BlockPos pos, int range, MSG message) {
        net.send(PacketDistributor.NEAR.with(
                () -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), range,
                        world.func_234923_W_())),
                message);
    }

    public static <MSG> void sendTracking(World world, BlockPos pos, MSG message) {
        net.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), message);
    }
}
