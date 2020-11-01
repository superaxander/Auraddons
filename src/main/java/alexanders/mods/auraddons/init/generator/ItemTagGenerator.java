package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.CuriosCompat;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider,
                            ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagsProvider, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {

        this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
        this.copy(BlockTags.WALLS, ItemTags.WALLS);
        this.copy(BlockTags.FENCES, ItemTags.FENCES);
        this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        this.copy(BlockTags.SLABS, ItemTags.SLABS);

        if (Auraddons.instance.curiosLoaded) {
            CuriosCompat.addItemTags(this);
        }
    }

    @Nonnull
    public TagsProvider.Builder<Item> getBuilder(@Nonnull ITag.INamedTag<Item> tagIn) {
        return super.getOrCreateBuilder(tagIn);
    }
}
