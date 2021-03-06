package alexanders.mods.auraddons;

import com.google.common.collect.ImmutableMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

import javax.annotation.Nullable;

public interface IProxy {
    void postInit();

    @Nullable
    IAnimationStateMachine loadASM(ResourceLocation resourceLocation, ImmutableMap<String, ITimeValue> map);

    <T extends TileEntity> void registerAnimationTESR(TileEntityType<T> type);
}
