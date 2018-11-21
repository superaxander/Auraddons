package alexanders.mods.auraddons;

import alexanders.mods.auraddons.init.ModItems;
import baubles.api.BaublesApi;
import com.google.common.collect.ImmutableMap;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

public class ClientProxy implements IProxy {
    public static final ResourceLocation OVERLAYS = new ResourceLocation(NaturesAuraAPI.MOD_ID, "textures/gui/overlays.png");

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            ScaledResolution res = event.getResolution();
            if (mc.player != null) {
                ItemStack cache = ItemStack.EMPTY;

                //if (Compat.baubles) {
                IItemHandler baubles = BaublesApi.getBaublesHandler(mc.player);
                for (int i = 0; i < baubles.getSlots(); i++) {
                    ItemStack slot = baubles.getStackInSlot(i);
                    if (!slot.isEmpty()) {
                        if (slot.getItem() == ModItems.creativeAuraCache) cache = slot;
                    }
                }
                //}

                if (cache.isEmpty()) {
                    for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                        ItemStack slot = mc.player.inventory.getStackInSlot(i);
                        if (!slot.isEmpty()) {
                            if (slot.getItem() == ModItems.creativeAuraCache) cache = slot;
                        }
                    }
                }

                if (!cache.isEmpty()) {
                    IAuraContainer container = cache.getCapability(NaturesAuraAPI.capAuraContainer, null);
                    if (container != null) {
                        int width = MathHelper.ceil(container.getStoredAura() / (float) container.getMaxAura() * 80);
                        int x = res.getScaledWidth() / 2 - 173 - (mc.player.getHeldItemOffhand().isEmpty() ? 0 : 29);
                        int y = res.getScaledHeight() - 8;

                        GlStateManager.pushMatrix();

                        int color = container.getAuraColor();
                        GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F);
                        mc.getTextureManager().bindTexture(OVERLAYS);
                        if (width < 80) Gui.drawModalRectWithCustomSizedTexture(x + width, y, width, 0, 80 - width, 6, 256, 256);
                        if (width > 0) Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 6, width, 6, 256, 256);

                        float scale = 0.75F;
                        GlStateManager.scale(scale, scale, scale);
                        String s = cache.getDisplayName();
                        mc.fontRenderer.drawString(s, (x + 80) / scale - mc.fontRenderer.getStringWidth(s), (y - 7) / scale, color, true);

                        GlStateManager.color(1F, 1F, 1F);
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
    }


    @Override
    public void registerItemModel(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), id));
    }

    @Override
    public <T extends TileEntity> void registerTESR(Class<T> te, TileEntitySpecialRenderer<T> tesr) {
        ClientRegistry.bindTileEntitySpecialRenderer(te, tesr);
    }

    @Nullable
    @Override
    public IAnimationStateMachine loadASM(ResourceLocation resourceLocation, ImmutableMap<String, ITimeValue> map) {
        return ModelLoaderRegistry.loadASM(resourceLocation, map);
    }
    
    @Override
    public void runLater(Runnable runnable) {
        Minecraft.getMinecraft().addScheduledTask(runnable);
    }

    @Override
    public <T extends Comparable<T>> void ignoreState(Block block, IProperty<T> property) {
        ModelLoader.setCustomStateMapper(block, (new StateMap.Builder()).ignore(property).build());
    }
}
