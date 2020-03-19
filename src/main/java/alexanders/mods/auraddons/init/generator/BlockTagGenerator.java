package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.init.ModBlocks;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getBuilder(BlockTags.FENCES).add(ModBlocks.ancientFence);
        getBuilder(BlockTags.WOODEN_FENCES).add(ModBlocks.ancientFence);
        getBuilder(BlockTags.WALLS).add(ModBlocks.goldBrickWall, ModBlocks.goldNetherBrickWall, ModBlocks.infusedBrickWall, ModBlocks.infusedStoneWall);
        getBuilder(BlockTags.SLABS).add(ModBlocks.goldBrickSlab, ModBlocks.goldNetherBrickSlab);
        getBuilder(BlockTags.STAIRS).add(ModBlocks.goldBrickStairs, ModBlocks.goldNetherBrickStairs);


    }

    @Nonnull
    public Tag.Builder<Block> getBuilder(@Nonnull Tag<Block> tagIn) {
        return super.getBuilder(tagIn);
    }
}
