package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.block.BlockAuraTransporter;
import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModPackets;
import alexanders.mods.auraddons.net.ConnectionPacket;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TileAuraTransporter extends TileEntity implements ITickableTileEntity {
    @Nullable
    public BlockPos other = null;

    public TileAuraTransporter() {
        super(ModBlocks.tileAuraTransporter);
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
        super.read(state, compound);
        readDestination(compound);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        writeDestination(compound);
        return super.write(compound);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        ModPackets.sendTracking(world, pos, new ConnectionPacket(pos, other));
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        writeDestination(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
        super.handleUpdateTag(state, compound);
        readDestination(compound);
    }

    private void readDestination(CompoundNBT compound) {
        if (compound.contains("destinationPos")) {
            other = BlockPos.fromLong(compound.getLong("destinationPos"));
        }
    }

    private void writeDestination(CompoundNBT compound) {
        if (other != null) {
            compound.putLong("destinationPos", other.toLong());
        }
    }

    private void addAura(int amount) {
        if (world != null) {
            BlockPos spot = NaturesAuraAPI.instance().getLowestAuraDrainSpot(world, pos,
                    ModConfig.aura.auraTransporterStoreRange, pos);
            IAuraChunk chunk = IAuraChunk.getAuraChunk(world, spot);
            chunk.storeAura(spot, amount);
        }
    }

    @Override
    public void tick() {
        if (world != null && !world.isRemote && world.getGameTime() % 40 == 2 && other != null && world.isAreaLoaded(other, 0)) {
            TileEntity dest = world.getTileEntity(other);
            double distance = Math.sqrt(other.distanceSq(pos));
            if (distance <= ModConfig.aura.auraTransporterRange && dest instanceof TileAuraTransporter) {
                if (!world.isRemote && world.getBlockState(pos).get(BlockAuraTransporter.SENDING) && !world.getBlockState(other).get(BlockAuraTransporter.SENDING)) {
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
}
