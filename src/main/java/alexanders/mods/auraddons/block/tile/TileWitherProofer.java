package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.init.ModConfig;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TileWitherProofer extends TileEntity {
    private static final ArrayList<TileWitherProofer> listenerList = new ArrayList<>();

    public boolean powered = false;

    public TileWitherProofer() {
        synchronized (listenerList) {
            listenerList.add(this);
        }
    }

    @SubscribeEvent
    public static void onMobGriefEvent(EntityMobGriefingEvent event) {
        final double rangeSq = ModConfig.general.witherProoferRange * ModConfig.general.witherProoferRange;
        Entity entity = event.getEntity();
        if (entity instanceof EntityWither) {
            synchronized (listenerList) {
                for (TileWitherProofer te : listenerList) {
                    if (!te.powered && te.pos.distanceSq(entity.posX, entity.posY, entity.posZ) <= rangeSq && te.tryPrevent()) {
                        event.setResult(Event.Result.DENY);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        powered = compound.getBoolean("powered");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("powered", powered);
        return compound;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        synchronized (listenerList) {
            listenerList.remove(this);
        }
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        synchronized (listenerList) {
            listenerList.remove(this);
        }
    }

    private boolean tryPrevent() {
        BlockPos spot = IAuraChunk.getHighestSpot(this.world, this.pos, 25, this.pos);
        IAuraChunk.getAuraChunk(this.world, spot).drainAura(spot, ModConfig.aura.witherProoferCost);
        return false;
    }
}
