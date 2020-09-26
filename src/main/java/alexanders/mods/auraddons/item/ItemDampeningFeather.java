package alexanders.mods.auraddons.item;

import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.render.ITrinketItem;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemDampeningFeather extends ItemSimple implements ITrinketItem {
    public ItemDampeningFeather() {
        super(new Properties().maxStackSize(1), ModNames.ITEM_DAMPING_FEATHER);
    }


    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull Entity entityIn, int itemSlot,
                              boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof PlayerEntity && entityIn.fallDistance >= 2.5) {
            if (NaturesAuraAPI.instance().extractAuraFromPlayer((PlayerEntity) entityIn,
                    (int) Math.ceil(ModConfig.aura.dampeningFeatherAuraPerMeter * 2.5), true)) {
                entityIn.fallDistance -= 2.5;
                NaturesAuraAPI.instance().extractAuraFromPlayer((PlayerEntity) entityIn,
                        (int) Math.ceil(ModConfig.aura.dampeningFeatherAuraPerMeter * 2.5), false);
            }
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(ItemStack stack, PlayerEntity player, RenderType type, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int packedLight, boolean isHolding) {
        if (type == RenderType.BODY && !isHolding) {
            boolean chest = !player.inventory.armorInventory.get(EquipmentSlotType.CHEST.getIndex()).isEmpty();
            boolean legs = !player.inventory.armorInventory.get(EquipmentSlotType.LEGS.getIndex()).isEmpty();
            matrixStack.translate(-0.15F, 0.3F, chest ? -0.195F : (legs ? -0.165F : -0.1475F));
            matrixStack.scale(0.25F, 0.25F, 0.25F);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
            Minecraft.getInstance()
                     .getItemRenderer()
                     .renderItem(stack, ItemCameraTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, renderBuffer);
        }
    }
}
