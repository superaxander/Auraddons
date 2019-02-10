package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileFreezer;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.render.IVisualizable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFreezer extends BlockBase implements ITileEntityProvider, IVisualizable {
    public BlockFreezer() {
        super(ModNames.BLOCK_FREEZER, Material.ROCK);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileFreezer();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.updateRedstoneState(worldIn, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.updateRedstoneState(worldIn, pos);
    }

    private void updateRedstoneState(@Nonnull World world, BlockPos pos) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileFreezer) {
                ((TileFreezer) tile).isPowered = world.isBlockIndirectlyGettingPowered(pos) > 0;
            }
        }
    }

    @Override
    public AxisAlignedBB getVisualizationBounds(World world, BlockPos pos) {
        return new AxisAlignedBB(pos).grow(1); //TODO: Also factor this out into a config variable
    }

    @Override
    public int getVisualizationColor(World world, BlockPos pos) {
        return 0;
    }
}
