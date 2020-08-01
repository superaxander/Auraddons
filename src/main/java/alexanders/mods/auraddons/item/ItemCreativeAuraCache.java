package alexanders.mods.auraddons.item;

import alexanders.mods.auraddons.aura.CreativeAuraContainer;
import alexanders.mods.auraddons.init.ModNames;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;
import de.ellpeck.naturesaura.api.aura.item.IAuraRecharge;
import de.ellpeck.naturesaura.api.render.ITrinketItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCreativeAuraCache extends ItemSimple implements ITrinketItem {

    public ItemCreativeAuraCache() {
        super(new Properties().maxStackSize(1), ModNames.ITEM_CREATIVE_AURA_CACHE);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stackIn, World worldIn, @Nonnull Entity entityIn, int itemSlot,
                              boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            if (player.isShiftKeyDown()) {
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    final LazyOptional<IAuraRecharge> optStack = stack.getCapability(NaturesAuraAPI.capAuraRecharge,
                            null);
                    if (optStack.isPresent()) {
                        IAuraContainer container = stackIn.getCapability(NaturesAuraAPI.capAuraContainer,
                                null).orElseThrow(IllegalStateException::new);
                        int finalI = i;
                        optStack.ifPresent(it -> it.rechargeFromContainer(container, itemSlot, finalI,
                                player.inventory.currentItem == finalI));
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            private final IAuraContainer container = new CreativeAuraContainer();

            @SuppressWarnings("unchecked")
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
                if (capability == NaturesAuraAPI.capAuraContainer) {
                    return LazyOptional.of(() -> (T) this.container);
                } else {
                    return LazyOptional.empty();
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(ItemStack stack, PlayerEntity player, RenderType type, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int packedLight, boolean isHolding) {
        if (type == RenderType.BODY && !isHolding) {
            boolean chest = !player.inventory.armorInventory.get(EquipmentSlotType.CHEST.getIndex()).isEmpty();
            boolean legs = !player.inventory.armorInventory.get(EquipmentSlotType.LEGS.getIndex()).isEmpty();
            matrixStack.translate(-0.15F, 0.65F, chest ? -0.195F : (legs ? -0.165F : -0.1475F));
            matrixStack.scale(0.25F, 0.25F, 0.25F);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer()
                    .renderItem(stack, ItemCameraTransforms.TransformType.GROUND, packedLight, OverlayTexture.DEFAULT_LIGHT, matrixStack, renderBuffer);
        }
    }
}
