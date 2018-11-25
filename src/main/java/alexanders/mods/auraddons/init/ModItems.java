package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.item.ItemCreativeAuraCache;
import alexanders.mods.auraddons.item.ItemDampeningFeather;
import alexanders.mods.auraddons.item.ItemSimple;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@GameRegistry.ObjectHolder(MOD_ID)
public final class ModItems {
    public static ArrayList<Item> itemRegistry = new ArrayList<>();

    @GameRegistry.ObjectHolder(ModNames.ITEM_CREATIVE_AURA_CACHE)
    public static Item creativeAuraCache;

    @GameRegistry.ObjectHolder(ModNames.ITEM_DAMPING_FEATHER)
    public static Item dampeningFeather;

    @GameRegistry.ObjectHolder(ModNames.ITEM_SKY_FEATHER)
    public static Item skyFeather;

    @GameRegistry.ObjectHolder(ModNames.ITEM_SKY_BOTTLE)
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
