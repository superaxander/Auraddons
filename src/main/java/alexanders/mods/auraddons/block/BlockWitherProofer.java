package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileWitherProofer;
import alexanders.mods.auraddons.init.ModNames;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWitherProofer extends BlockBase implements ITileEntityProvider {
    public BlockWitherProofer() {
        super(ModNames.BLOCK_WITHER_PROOFER, Material.CLOTH);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileWitherProofer();
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
            if (tile instanceof TileWitherProofer) {
                ((TileWitherProofer) tile).powered = world.isBlockIndirectlyGettingPowered(pos) > 0;
            }
        }
    }
}
