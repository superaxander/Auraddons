package alexanders.mods.auraddons.init.generator;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModItems;
import javax.annotation.Nonnull;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Item item : ModItems.itemRegistry) {
            final ResourceLocation registryName = item.getRegistryName();
            if (registryName != null) {
                String name = registryName.getPath();
                if (item instanceof BlockItem) {
                    Block block = ((BlockItem) item).getBlock();
                    if (!(block instanceof FenceBlock) && !(block instanceof FenceGateBlock) && !(block instanceof WallBlock) && !(block instanceof LadderBlock) && block != ModBlocks.auraTransporter) {
                        this.withExistingParent(name, this.modLoc("block/" + name));
                    }
                } else {
                    this.withExistingParent(name, "item/generated").texture("layer0", "items/" + name);
                }
            }
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return "Auraddons Item Models";
    }
}
