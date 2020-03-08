package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModNames;
import net.minecraft.block.*;
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
            } else if (block == ModBlocks.disruptionCatalyst) { //TODO: Give this its own class
                this.simpleBlock(block, this.models().cubeAll(ModNames.BLOCK_DISRUPTION_CATALYST, modLoc("blocks/" + ModNames.BLOCK_DISRUPTION_CATALYST)));
            } else if (!(block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof WallBlock || block instanceof LadderBlock)) { //TODO: Make sure these are in a different registry or something because it seems quite ridiculous to do this for each block
                this.simpleBlock(block);
            }
        }
    }
}
