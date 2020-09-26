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
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

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
                    if (block == ModBlocks.infusedStoneWall) {
                        this.wallInventory(name, new ResourceLocation("naturesaura", "block/infused_stone"));
                    } else if (block == ModBlocks.infusedBrickWall) {
                        this.wallInventory(name, new ResourceLocation("naturesaura", "block/infused_brick"));
                    } else if (block == ModBlocks.goldBrickWall) {
                        this.wallInventory(name, new ResourceLocation("naturesaura", "block/gold_brick"));
                    } else if (block == ModBlocks.goldNetherBrickWall) {
                        this.wallInventory(name, new ResourceLocation("naturesaura", "block/gold_nether_brick"));
                    } else if (!(block instanceof FenceBlock) && !(block instanceof FenceGateBlock) && !(block instanceof LadderBlock) && !(block instanceof SlabBlock) && !(block instanceof StairsBlock) && block != ModBlocks.auraTransporter) {
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
        return "Item Models";
    }
}
