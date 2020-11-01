package alexanders.mods.auraddons.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ObjectHolder;

import static alexanders.mods.auraddons.Constants.MOD_NAME;

public final class ModTabs {
    @ObjectHolder("naturesaura:aura_bottle")
    public static Item auraBottle;

    public static final ItemGroup MAIN_TAB = new ItemGroup(MOD_NAME) {
        @Override
        public ItemStack createIcon() {
            if (ModItems.creativeAuraCache == null)
                if (ModBlocks.autoWrath == null) if (auraBottle == null) return new ItemStack(Items.GLASS_BOTTLE);
                else return new ItemStack(auraBottle);
                else return new ItemStack(ModBlocks.autoWrath);
            else return new ItemStack(ModItems.creativeAuraCache);
        }
    };
}
