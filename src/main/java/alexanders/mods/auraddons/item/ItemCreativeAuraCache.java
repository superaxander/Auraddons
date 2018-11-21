package alexanders.mods.auraddons.item;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.aura.CreativeAuraContainer;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;
import de.ellpeck.naturesaura.api.render.ITrinketItem;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCreativeAuraCache extends ItemSimple implements ITrinketItem {

    public ItemCreativeAuraCache() {
        super(ModNames.ITEM_CREATIVE_AURA_CACHE);
    }

    @Override
    public void onUpdate(ItemStack stackIn, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.isSneaking()) {
                ItemStack stack = player.getHeldItemMainhand();
                if (stack.hasCapability(NaturesAuraAPI.capAuraRecharge, null)) {
                    IAuraContainer container = stackIn.getCapability(NaturesAuraAPI.capAuraContainer, null);
                    Objects.requireNonNull(stack.getCapability(NaturesAuraAPI.capAuraRecharge, null)).rechargeFromContainer(container);
                }
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ICapabilityProvider() {
            private final IAuraContainer container = new CreativeAuraContainer();

            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == NaturesAuraAPI.capAuraContainer;
            }

            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                if (capability == NaturesAuraAPI.capAuraContainer) {
                    return NaturesAuraAPI.capAuraContainer.cast(this.container);
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(ItemStack stack, EntityPlayer player, ITrinketItem.RenderType type, boolean isHolding) {
        if (type == RenderType.BODY && !isHolding) {
            boolean chest = !player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex()).isEmpty();
            boolean legs = !player.inventory.armorInventory.get(EntityEquipmentSlot.LEGS.getIndex()).isEmpty();
            GlStateManager.translate(-0.15F, 0.65F, chest ? -0.195F : (legs ? -0.165F : -0.1475F));
            GlStateManager.scale(0.25F, 0.25F, 0.25F);
            GlStateManager.rotate(180F, 1F, 0F, 0F);
            Auraddons.proxy.renderItemInWorld(stack);
        }
    }
}
