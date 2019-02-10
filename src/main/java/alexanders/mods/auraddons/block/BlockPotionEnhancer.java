package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TilePotionEnhancer;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.render.IVisualizable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPotionEnhancer extends BlockBase implements ITileEntityProvider, IVisualizable {
    public BlockPotionEnhancer() {
        super(ModNames.BLOCK_POTION_ENHANCER, Material.WOOD);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
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
}
