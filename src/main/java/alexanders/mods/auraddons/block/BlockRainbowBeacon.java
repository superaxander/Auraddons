package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileRainbowBeacon;
import alexanders.mods.auraddons.init.ModNames;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRainbowBeacon extends BlockBase implements ITileEntityProvider {
    public BlockRainbowBeacon() {
        super(ModNames.BLOCK_RAINBOW_BEACON, Material.GLASS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRainbowBeacon && ((TileRainbowBeacon) te).colorMultiplier[0] != -1) return ((TileRainbowBeacon)te).colorMultiplier;
        else return super.getBeaconColorMultiplier(state, world, pos, beaconPos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileRainbowBeacon();
    }
}
