package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.item.ItemCreativeAuraCache;
import alexanders.mods.auraddons.item.ItemDampeningFeather;
import alexanders.mods.auraddons.item.ItemSimple;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.ArrayList;

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
        itemRegistry.add(creativeAuraCache = new ItemCreativeAuraCache());
        itemRegistry.add(dampeningFeather = new ItemDampeningFeather());
        itemRegistry.add(skyFeather = new ItemSimple(ModNames.ITEM_SKY_FEATHER) {
            @Override
            public boolean hasEffect(@Nullable ItemStack stack) {
                return true;
            }
        });
        itemRegistry.add(skyBottle = new ItemSimple(ModNames.ITEM_SKY_BOTTLE) {
            @Override
            public boolean hasEffect(@Nullable ItemStack stack) {
                return true;
            }
        });
    }
}
