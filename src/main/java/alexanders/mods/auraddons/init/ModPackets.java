package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.net.JumpPacket;
import alexanders.mods.auraddons.net.ParticlePacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModPackets {
    private static SimpleNetworkWrapper net;

    public static void init() {
        net = new SimpleNetworkWrapper(Constants.MOD_ID);
        net.registerMessage(ParticlePacket.Handler.class, ParticlePacket.class, 0, Side.CLIENT);
        net.registerMessage(JumpPacket.Handler.class, JumpPacket.class, 1, Side.CLIENT);
    }

    public static void sendAround(World world, BlockPos pos, int range, IMessage message) {
        net.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
    }
    
    public static void sendTracking(World world, BlockPos pos, IMessage message) {
        net.sendToAllTracking(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 0));
    }
}
