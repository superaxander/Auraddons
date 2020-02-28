package alexanders.mods.auraddons.block;

import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public abstract class BlockContainerBase extends ContainerBlock {
    public BlockContainerBase(String name, Material material) {
        this(name, material, material.getColor());
    }


    public BlockContainerBase(String unlocalizedName, Material material, MaterialColor mapColor) {
        this(unlocalizedName, Properties.create(material, mapColor).hardnessAndResistance(2.0F, 5.0F));
    }

    public BlockContainerBase(String unlocalizedName, Properties properties) {
        super(properties);
        setRegistryName(unlocalizedName);
    }
}
