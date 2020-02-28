package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.item.ItemCreativeAuraCache;
import alexanders.mods.auraddons.item.ItemDampeningFeather;
import alexanders.mods.auraddons.item.ItemSimple;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@ObjectHolder(MOD_ID)
public final class ModItems {
    public static ArrayList<Item> itemRegistry = new ArrayList<>();

    @ObjectHolder(ModNames.ITEM_CREATIVE_AURA_CACHE)
    public static Item creativeAuraCache;

    @ObjectHolder(ModNames.ITEM_DAMPING_FEATHER)
    public static Item dampeningFeather;

    @ObjectHolder(ModNames.ITEM_SKY_FEATHER)
    public static Item skyFeather;

    @ObjectHolder(ModNames.ITEM_SKY_BOTTLE)
    public static Item skyBottle;

    public static void init() {
        if (ModConfig.items.enableCreativeAuraCache) itemRegistry.add(new ItemCreativeAuraCache());
        if (ModConfig.items.enableDampeningFeather) itemRegistry.add(new ItemDampeningFeather());
        if (ModConfig.items.enableSkyFeather) itemRegistry.add(new ItemSimple(ModNames.ITEM_SKY_FEATHER) {
            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        });
        if (ModConfig.items.enableSkyBottle) itemRegistry.add(new ItemSimple(ModNames.ITEM_SKY_BOTTLE) {
            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        });
    }
}
