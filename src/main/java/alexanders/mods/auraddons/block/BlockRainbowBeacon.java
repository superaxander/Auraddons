package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.block.tile.TileRainbowBeacon;
import alexanders.mods.auraddons.init.ModNames;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.IStateProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRainbowBeacon extends BlockContainerBase implements IStateProvider {
    public BlockRainbowBeacon() {
        super(ModNames.BLOCK_RAINBOW_BEACON,
                Properties.create(Material.GLASS).notSolid().hardnessAndResistance(2.0F, 5.0F));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockRenderType getRenderType(@Nullable BlockState state) {
        return BlockRenderType.MODEL;
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
        generator.simpleBlock(this, generator.models().cubeAll(ModNames.BLOCK_RAINBOW_BEACON, generator.modLoc("blocks/" + ModNames.BLOCK_RAINBOW_BEACON)));
    }
}
