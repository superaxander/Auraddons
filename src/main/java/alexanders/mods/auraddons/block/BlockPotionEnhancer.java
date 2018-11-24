package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TilePotionEnhancer;
import alexanders.mods.auraddons.init.ModNames;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPotionEnhancer extends BlockBase implements ITileEntityProvider {
    public BlockPotionEnhancer() {
        super(ModNames.BLOCK_POTION_ENHANCER, Material.WOOD);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TilePotionEnhancer();
    }
}
