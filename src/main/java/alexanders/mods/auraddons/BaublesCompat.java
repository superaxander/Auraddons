package alexanders.mods.auraddons;

import alexanders.mods.auraddons.init.ModItems;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.BaublesCapabilities;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class BaublesCompat {
    private static final IBauble cache = new IBauble() {
        @Override
        public BaubleType getBaubleType(ItemStack itemstack) {
            return BaubleType.BELT;
        }

        @Override
        public void onWornTick(ItemStack stack, EntityLivingBase player) {
            stack.getItem().onUpdate(stack, player.world, player, -1, false);
        }

        @Override
        public boolean willAutoSync(ItemStack stack, EntityLivingBase player) {
            return true;
        }
    };
    private static final IBauble feather = new IBauble() {
        @Override
        public BaubleType getBaubleType(ItemStack itemstack) {
            return BaubleType.RING;
        }

        @Override
        public void onWornTick(ItemStack stack, EntityLivingBase player) {
            stack.getItem().onUpdate(stack, player.world, player, -1, false);
        }

        @Override
        public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
            return true;
        }
    };

    public static void init() {

    }

    @SideOnly(Side.CLIENT)
    public static IItemHandler getItemHandler() {
        return BaublesApi.getBaublesHandler(Minecraft.getMinecraft().player);
    }

    @SubscribeEvent
    public void attachBaubles(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == ModItems.creativeAuraCache) {
            event.addCapability(new ResourceLocation(MOD_ID, "bauble"), new ICapabilityProvider() {
                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE ? BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(cache) : null;
                }
            });
        } else if (event.getObject().getItem() == ModItems.dampeningFeather) {
            event.addCapability(new ResourceLocation(MOD_ID, "bauble"), new ICapabilityProvider() {
                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == BaublesCapabilities.CAPABILITY_ITEM_BAUBLE ? BaublesCapabilities.CAPABILITY_ITEM_BAUBLE.cast(feather) : null;
                }
            });
        }
    }
}
