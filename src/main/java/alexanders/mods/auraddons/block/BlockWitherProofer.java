package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileWitherProofer;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.IStateProvider;
import de.ellpeck.naturesaura.api.render.IVisualizable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockWitherProofer extends BlockContainerBase implements IVisualizable, IStateProvider {
    public BlockWitherProofer() {
        super(ModNames.BLOCK_WITHER_PROOFER, Material.WOOL);
    }

    @Override
    public void provideState(BlockStateGenerator generator) {
        generator.simpleBlock(this, generator.models()
                .cubeColumn(ModNames.BLOCK_WITHER_PROOFER, generator.modLoc("blocks/" + ModNames.BLOCK_WITHER_PROOFER), generator.mcLoc("block/white_wool")));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new TileWitherProofer();
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        this.updateRedstoneState(world, pos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        this.updateRedstoneState(world, pos);
    }

    private void updateRedstoneState(@Nonnull IWorldReader world, BlockPos pos) {
        if (!world.isRemote()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileWitherProofer && world instanceof World) {
                ((TileWitherProofer) tile).powered = ((World) world).getRedstonePowerFromNeighbors(pos) > 0;
            }
        }
    }

    @Override
    public AxisAlignedBB getVisualizationBounds(World world, BlockPos pos) {
        return new AxisAlignedBB(pos).grow(ModConfig.general.witherProoferRange);
    }

    @Override
    public int getVisualizationColor(World world, BlockPos pos) {
        return 0x738AD0;
    }
}
