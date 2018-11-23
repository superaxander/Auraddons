package alexanders.mods.auraddons.item;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.render.ITrinketItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDampeningFeather extends ItemSimple implements ITrinketItem {
    public ItemDampeningFeather() {
        super(ModNames.ITEM_DAMPING_FEATHER);
        setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer && entityIn.fallDistance >= 2.5) {
            if (NaturesAuraAPI.instance().extractAuraFromPlayer((EntityPlayer) entityIn, (int) Math.ceil(ModConfig.aura.dampeningFeatherAuraPerMeter * 2.5), true)) {
                entityIn.fallDistance -= 2.5;
                NaturesAuraAPI.instance().extractAuraFromPlayer((EntityPlayer) entityIn, (int) Math.ceil(ModConfig.aura.dampeningFeatherAuraPerMeter * 2.5), false);
            }
        }
    }

    @Override
    public void render(ItemStack stack, EntityPlayer player, RenderType type, boolean isHolding) {
        if (type == RenderType.BODY && !isHolding) {
            boolean chest = !player.inventory.armorInventory.get(EntityEquipmentSlot.CHEST.getIndex()).isEmpty();
            boolean legs = !player.inventory.armorInventory.get(EntityEquipmentSlot.LEGS.getIndex()).isEmpty();
            GlStateManager.translate(-0.15F, 0.3F, chest ? -0.195F : (legs ? -0.165F : -0.1475F));
            GlStateManager.scale(0.25F, 0.25F, 0.25F);
            GlStateManager.rotate(180F, 1F, 0F, 0F);
            Auraddons.proxy.renderItemInWorld(stack);
        }
    }
}
