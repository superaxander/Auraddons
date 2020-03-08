package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.CuriosCompat;
import javax.annotation.Nonnull;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;

public class ItemTagGenerator extends ItemTagsProvider {
    public ItemTagGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        if (Auraddons.instance.curiosLoaded) {
            CuriosCompat.addItemTags(this);
        }
    }

    @Nonnull
    public Tag.Builder<Item> getBuilder(@Nonnull Tag<Item> tagIn) {
        return super.getBuilder(tagIn);
    }
}
