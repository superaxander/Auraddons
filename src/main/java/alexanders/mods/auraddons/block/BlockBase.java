package alexanders.mods.auraddons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

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
}
