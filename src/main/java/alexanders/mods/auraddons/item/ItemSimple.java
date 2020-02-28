package alexanders.mods.auraddons.item;

import alexanders.mods.auraddons.init.ModTabs;
import net.minecraft.item.Item;

public class ItemSimple extends Item {
    public ItemSimple(String unlocalizedName) {
        this(new Properties(), unlocalizedName);
    }

    public ItemSimple(Properties properties, String unlocalizedName) {
        super(properties.group(ModTabs.MAIN_TAB));
        setRegistryName(unlocalizedName);
    }
}
