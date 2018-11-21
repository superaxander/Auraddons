package alexanders.mods.auraddons;

import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ServerProxy implements IProxy {
    @Override
    public void registerItemModel(Item item, int meta, String id) {
    }

    @Override
    public <T extends TileEntity> void registerTESR(Class<T> te, TileEntitySpecialRenderer<T> tesr) {

    }

    @Override
    @Nullable
    public IAnimationStateMachine loadASM(ResourceLocation resourceLocation, ImmutableMap<String, ITimeValue> map) {
        return null;
    }

    @Override
    public void runLater(Runnable runnable) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
    }

    @Override
    public <T extends Comparable<T>> void ignoreState(Block block, IProperty<T> powered) {

    }

    @Override
    public void renderItemInWorld(ItemStack stack) {
        
    }
}
