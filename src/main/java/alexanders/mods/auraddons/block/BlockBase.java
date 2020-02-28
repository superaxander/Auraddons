package alexanders.mods.auraddons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockBase extends Block {


    public BlockBase(String name, Material material) {
        this(name, material, material.getColor());
    }


    public BlockBase(String unlocalizedName, Material material, MaterialColor mapColor) {
        this(unlocalizedName, Properties.create(material, mapColor).hardnessAndResistance(2.0F, 5.0F));
    }

    public BlockBase(String unlocalizedName, Properties properties) {
        super(properties);
        setRegistryName(unlocalizedName);
    }

    @Override
    public void fillItemGroup(ItemGroup p_149666_1_, NonNullList<ItemStack> p_149666_2_) {
        super.fillItemGroup(p_149666_1_, p_149666_2_);
    }
}
