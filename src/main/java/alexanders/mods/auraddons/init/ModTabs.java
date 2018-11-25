package alexanders.mods.auraddons.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_NAME;

public final class ModTabs {
    @GameRegistry.ObjectHolder("naturesaura:aura_bottle")
    public static Item auraBottle;

    public static final CreativeTabs MAIN_TAB = new CreativeTabs(MOD_NAME) {
        @Override
        public ItemStack getTabIconItem() {
            if (ModItems.creativeAuraCache == null) if (ModBlocks.autoWrath == null) if (auraBottle == null) return new ItemStack(Items.GLASS_BOTTLE);
            else return new ItemStack(auraBottle);
            else return new ItemStack(ModBlocks.autoWrath);
            else return new ItemStack(ModItems.creativeAuraCache);
        }
    };
}
