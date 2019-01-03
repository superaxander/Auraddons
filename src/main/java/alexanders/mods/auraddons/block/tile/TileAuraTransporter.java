package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.block.BlockAuraTransporter;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModPackets;
import alexanders.mods.auraddons.net.ConnectionPacket;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileAuraTransporter extends TileEntity implements ITickable {
    @Nullable
    public BlockPos other = null;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readDestination(compound);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        writeDestination(compound);
        return super.writeToNBT(compound);
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        writeDestination(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound compound) {
        super.handleUpdateTag(compound);
        readDestination(compound);
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    private void readDestination(NBTTagCompound compound) {
        if (compound.hasKey("destinationPos")) {
            other = BlockPos.fromLong(compound.getLong("destinationPos"));
        }
    }

    private void writeDestination(NBTTagCompound compound) {
        if (other != null) {
            compound.setLong("destinationPos", other.toLong());
        }
    }

    private void addAura(int amount) {
        BlockPos spot = NaturesAuraAPI.instance().getLowestAuraDrainSpot(world, pos, ModConfig.aura.auraTransporterStoreRange, pos);
        IAuraChunk chunk = IAuraChunk.getAuraChunk(world, spot);
        chunk.storeAura(spot, amount);
        System.out.println("Added " + amount + " aura");
    }

    @Override
    public void update() {
        if (!world.isRemote && world.getTotalWorldTime() % 40 == 2 && other != null && world.isBlockLoaded(other)) {
            TileEntity dest = world.getTileEntity(other);
            double distance = Math.sqrt(other.distanceSq(pos));
            if (distance <= ModConfig.aura.auraTransporterRange && dest instanceof TileAuraTransporter) {
                if (!world.isRemote && world.getBlockState(pos).getValue(BlockAuraTransporter.SENDING) && !world.getBlockState(other).getValue(BlockAuraTransporter.SENDING)) {
                    BlockPos spot = NaturesAuraAPI.instance().getHighestAuraDrainSpot(world, pos, ModConfig.aura.auraTransporterDrainRange, pos);
                    int aura = NaturesAuraAPI.instance().getAuraInArea(world, spot, 0);
                    if (aura >= ModConfig.aura.auraTransporterAuraAmount + IAuraChunk.DEFAULT_AURA) { // Only drain excess
                        IAuraChunk chunk = IAuraChunk.getAuraChunk(world, spot);
                        if (chunk.drainAura(spot, ModConfig.aura.auraTransporterAuraAmount, true, true) >= ModConfig.aura.auraTransporterAuraAmount) {
                            chunk.drainAura(spot, ModConfig.aura.auraTransporterAuraAmount, true, false);
                            ((TileAuraTransporter) dest).addAura(
                                    MathHelper.fastFloor(ModConfig.aura.auraTransporterAuraAmount * Math.pow(ModConfig.aura.auraTransporterAuraMultiplierPerBlock, distance)));
                        }
                    }
                }
            } else {
                other = null;
                markDirty();
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        ModPackets.sendTracking(world, pos, new ConnectionPacket(pos, other));
    }
}
