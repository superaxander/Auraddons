package alexanders.mods.auraddons.item;

import alexanders.mods.auraddons.init.ModTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class ItemSimple extends Item {
    public ItemSimple(String unlocalizedName) {
        this(unlocalizedName, ModTabs.MAIN_TAB);
    }

    public ItemSimple(String unlocalizedName, CreativeTabs tab) {
        setUnlocalizedName(MOD_ID + "." + unlocalizedName);
        setRegistryName(unlocalizedName);
        setCreativeTab(tab);
    }
}
