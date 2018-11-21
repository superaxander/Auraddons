package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.item.ItemCreativeAuraCache;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@GameRegistry.ObjectHolder(MOD_ID)
public final class ModItems {
    public static ArrayList<Item> itemRegistry = new ArrayList<>();

    @GameRegistry.ObjectHolder(ModNames.ITEM_CREATIVE_AURA_CACHE)
    public static Item creativeAuraCache;

    public static void init() {
        itemRegistry.add(new ItemCreativeAuraCache());
    }
}
