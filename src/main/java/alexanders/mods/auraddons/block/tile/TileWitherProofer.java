package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModConfig;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TileWitherProofer extends TileEntity {
    private static final ArrayList<TileWitherProofer> listenerList = new ArrayList<>();

    public boolean powered = false;

    public TileWitherProofer() {
        super(ModBlocks.tileWitherProofer);
        synchronized (listenerList) {
            listenerList.add(this);
        }
    }

    @SubscribeEvent
    public static void onMobGriefEvent(EntityMobGriefingEvent event) {
        final double rangeSq = ModConfig.general.witherProoferRange * ModConfig.general.witherProoferRange;
        Entity entity = event.getEntity();
        if (entity instanceof WitherEntity && entity.world != null && !entity.world.isRemote) {
            synchronized (listenerList) {
                for (Iterator<TileWitherProofer> iterator = listenerList.iterator(); iterator.hasNext(); ) {
                    TileWitherProofer te = iterator.next();
                    if (te.world != null && te.world.isRemote) {
                        iterator.remove();
                        continue;
                    }
                    if (!te.powered && te.pos.distanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ(), true) <= rangeSq && te
                            .tryPrevent()) { //TODO: What does the true here do?
                        event.setResult(Event.Result.DENY);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        powered = compound.getBoolean("powered");
    }

    @Override
    @Nonnull
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("powered", powered);
        return compound;
    }


    @Override
    public void remove() {
        super.remove();
        synchronized (listenerList) {
            listenerList.remove(this);
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        synchronized (listenerList) {
            listenerList.remove(this);
        }
    }

    private boolean tryPrevent() {
        if (world == null) return false;
        BlockPos spot = IAuraChunk.getHighestSpot(this.world, this.pos, 25, this.pos);
        return IAuraChunk.getAuraChunk(this.world, spot).drainAura(spot, ModConfig.aura.witherProoferCost) >= ModConfig.aura.witherProoferCost;
    }
}
