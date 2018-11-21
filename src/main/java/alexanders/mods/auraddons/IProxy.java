package alexanders.mods.auraddons;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

public interface IProxy {

    void registerItemModel(Item item, int meta, String id);

    <T extends TileEntity> void registerTESR(Class<T> te, TileEntitySpecialRenderer<T> tesr);

    @Nullable
    IAnimationStateMachine loadASM(ResourceLocation resourceLocation, ImmutableMap<String, ITimeValue> map);

    void runLater(Runnable runnable);

    <T extends Comparable<T>> void ignoreState(Block block, IProperty<T> powered);
}
