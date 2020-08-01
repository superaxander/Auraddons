package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileAutoWrath;
import alexanders.mods.auraddons.init.ModNames;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.IStateProvider;
import de.ellpeck.naturesaura.api.render.IVisualizable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.block.HorizontalBlock.HORIZONTAL_FACING;

public class BlockAutoWrath extends BlockContainerBase implements IVisualizable, IStateProvider {
    public BlockAutoWrath() {
        super(ModNames.BLOCK_AUTO_WRATH, Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 5.0F).notSolid());
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nullable BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rot) {
        return state.with(HORIZONTAL_FACING, rot.rotate(state.get(HORIZONTAL_FACING)));
    }

    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirrorIn) {
        return state.with(HORIZONTAL_FACING, mirrorIn.mirror(state.get(HORIZONTAL_FACING)));
    }

    @Override
    public void provideState(BlockStateGenerator generator) {
        //        generator.horizontalBlock(this, generator.modLoc());
        //        generator.simpleBlock(this, generator.models().getExistingFile(generator.modLoc(ModNames.BLOCK_AUTO_WRATH)));
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getPlayer() != null) return getDefaultState().with(HORIZONTAL_FACING, context.getPlayer().getHorizontalFacing().getOpposite());
        return getDefaultState();
    }

    @Override
    protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> container) {
        super.fillStateContainer(container);
        container.add(HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
        return new TileAutoWrath();
    }

    @Override
    public AxisAlignedBB getVisualizationBounds(World world, BlockPos pos) {
        return new AxisAlignedBB(pos).grow(4.5, 0, 4.5); // TODO: Factor this value out into a config variable
    }

    @Override
    public int getVisualizationColor(World world, BlockPos pos) {
        return 0xD63439;
    }
}
