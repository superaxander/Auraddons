package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.init.ModTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class BlockBase extends Block {


    public BlockBase(String unlocalizedName, Material material) {
        this(unlocalizedName, material, material.getMaterialMapColor(), ModTabs.MAIN_TAB);
    }

    public BlockBase(String unlocalizedName, Material material, CreativeTabs tab) {
        this(unlocalizedName, material, material.getMaterialMapColor(), tab);
    }

    public BlockBase(String unlocalizedName, Material material, MapColor mapColor) {
        this(unlocalizedName, material, mapColor, ModTabs.MAIN_TAB);
    }

    public BlockBase(String unlocalizedName, Material material, MapColor mapColor, CreativeTabs tab) {
        super(material, mapColor);
        setTranslationKey(MOD_ID + "." + unlocalizedName);
        setRegistryName(unlocalizedName);
        setCreativeTab(tab);
    }
}
