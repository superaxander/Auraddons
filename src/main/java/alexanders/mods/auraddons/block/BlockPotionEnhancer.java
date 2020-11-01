package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TilePotionEnhancer;
import alexanders.mods.auraddons.init.ModNames;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.IStateProvider;
import de.ellpeck.naturesaura.api.render.IVisualizable;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPotionEnhancer extends BlockContainerBase implements IStateProvider, IVisualizable {
    public BlockPotionEnhancer() {
        super(ModNames.BLOCK_POTION_ENHANCER, Material.WOOD);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nullable BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new TilePotionEnhancer();
    }

    @Override
    public AxisAlignedBB getVisualizationBounds(World world, BlockPos pos) {
        return new AxisAlignedBB(pos.down());
    }

    @Override
    public int getVisualizationColor(World world, BlockPos pos) {
        return 0x53D634;
    }

    @Override
    public void provideState(BlockStateGenerator generator) {

        generator.simpleBlock(this, generator.models().cubeBottomTop(ModNames.BLOCK_POTION_ENHANCER,
                generator.modLoc("blocks/" + ModNames.BLOCK_POTION_ENHANCER),
                generator.modLoc("blocks/" + ModNames.BLOCK_POTION_ENHANCER + "_top"),
                generator.modLoc("blocks/" + ModNames.BLOCK_POTION_ENHANCER + "_top")));
    }
}
