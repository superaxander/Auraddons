package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(DataGenerator generatorIn, ExistingFileHelper ex) {
        super(generatorIn, MOD_ID, ex);
    }

    @Override
    protected void registerTags() {
        getBuilder(BlockTags.FENCES).add(ModBlocks.ancientFence);
        getBuilder(BlockTags.WOODEN_FENCES).add(ModBlocks.ancientFence);
        getBuilder(BlockTags.WALLS).add(ModBlocks.goldBrickWall, ModBlocks.goldNetherBrickWall,
                ModBlocks.infusedBrickWall, ModBlocks.infusedStoneWall);
        getBuilder(BlockTags.SLABS).add(ModBlocks.goldBrickSlab, ModBlocks.goldNetherBrickSlab);
        getBuilder(BlockTags.STAIRS).add(ModBlocks.goldBrickStairs, ModBlocks.goldNetherBrickStairs);


    }

    @Nonnull
    public Builder<Block> getBuilder(@Nonnull ITag.INamedTag<Block> tagIn) {
        return super.getOrCreateBuilder(tagIn);
    }
}
