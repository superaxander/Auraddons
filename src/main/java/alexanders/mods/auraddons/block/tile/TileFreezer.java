package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModPackets;
import alexanders.mods.auraddons.net.ParticlePacket;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileFreezer extends TileEntity implements ITickable {
    public boolean isPowered = false;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        isPowered = compound.getBoolean("isPowered");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("isPowered", isPowered);
        return super.writeToNBT(compound);
    }

    @Override
    public void update() {
        if (!world.isRemote && (world.getTotalWorldTime() + 5) % 60 == 0) {
            if (IAuraChunk.getAuraInArea(this.world, this.pos, 25) < 1000)
                return;
            
            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        if(tryFreeze(pos.add(x, y, z)))
                            return;
                    }
                }
            }
        }
    }

    private boolean tryFreeze(BlockPos pos) {
        if(world.isBlockLoaded(pos)) {
            IBlockState state = world.getBlockState(pos);
            if(state.getBlock() == Blocks.WATER && state.getValue(BlockLiquid.LEVEL) == 0) {
                if(isPowered) {
                    BlockPos spot = IAuraChunk.getHighestSpot(this.world, this.pos, 25, this.pos);
                    IAuraChunk.getAuraChunk(this.world, spot).drainAura(spot, 80);

                    world.setBlockState(pos, ModBlocks.hardIce.getDefaultState());
                }else {
                    BlockPos spot = IAuraChunk.getHighestSpot(this.world, this.pos, 25, this.pos);
                    IAuraChunk.getAuraChunk(this.world, spot).drainAura(spot, 10);

                    world.setBlockState(pos, Blocks.ICE.getDefaultState());
                }
                ModPackets.sendAround(getWorld(), pos, 32, new ParticlePacket(ParticlePacket.Type.FREEZE, pos));
                //TODO: Maybe make a sound?
                return true;
            }else if(state.getBlock() == Blocks.AIR) {
                if(world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) {
                    BlockPos spot = IAuraChunk.getHighestSpot(this.world, this.pos, 25, this.pos);
                    IAuraChunk.getAuraChunk(this.world, spot).drainAura(spot, 5);
                    
                    world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());

                    ModPackets.sendAround(getWorld(), pos, 32, new ParticlePacket(ParticlePacket.Type.FREEZE, pos));
                    return true;
                }
            }
        }
        return false;
    }
}
