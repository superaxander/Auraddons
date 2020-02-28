package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Block block : ModBlocks.blockRegistry) {
            if (block instanceof IStateProvider) {
                ((IStateProvider) block).provideState(this);
            } else {
                this.simpleBlock(block);
            }
        }
    }
}
