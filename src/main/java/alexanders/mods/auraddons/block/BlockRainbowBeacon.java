package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileRainbowBeacon;
import alexanders.mods.auraddons.init.ModNames;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.IStateProvider;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class BlockRainbowBeacon extends BlockContainerBase implements IStateProvider {
    public BlockRainbowBeacon() {
        super(ModNames.BLOCK_RAINBOW_BEACON, Properties.create(Material.GLASS).notSolid().hardnessAndResistance(2.0F, 5.0F));
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRainbowBeacon && ((TileRainbowBeacon) te).colorMultiplier[0] != -1) return ((TileRainbowBeacon) te).colorMultiplier;
        else return super.getBeaconColorMultiplier(state, world, pos, beaconPos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new TileRainbowBeacon();
    }

    @Override
    public void provideState(BlockStateGenerator generator) {
        generator.simpleBlock(this, generator.models().getExistingFile(generator.modLoc(ModNames.BLOCK_RAINBOW_BEACON)));
    }
}
