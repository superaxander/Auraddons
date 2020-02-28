package alexanders.mods.auraddons;

import alexanders.mods.auraddons.init.ModItems;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.animation.TileEntityRendererAnimation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {
    public static final ResourceLocation OVERLAYS = new ResourceLocation(NaturesAuraAPI.MOD_ID, "textures/gui/overlays.png");

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            MainWindow window = event.getWindow();
            if (mc.player != null) {
                ItemStack cache = ItemStack.EMPTY;

                //                if (Auraddons.instance.baublesLoaded) {
                //                    IItemHandler baubles = BaublesCompat.getItemHandler();
                //                    for (int i = 0; i < baubles.getSlots(); i++) {
                //                        ItemStack slot = baubles.getStackInSlot(i);
                //                        if (!slot.isEmpty()) {
                //                            if (slot.getItem() == ModItems.creativeAuraCache) cache = slot;
                //                        }
                //                    }
                //                }

                if (cache.isEmpty()) {
                    for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                        ItemStack slot = mc.player.inventory.getStackInSlot(i);
                        if (!slot.isEmpty()) {
                            if (slot.getItem() == ModItems.creativeAuraCache) cache = slot;
                        }
                    }
                }

                if (!cache.isEmpty()) {
                    LazyOptional<IAuraContainer> container = cache.getCapability(NaturesAuraAPI.capAuraContainer, null);
                    ItemStack finalCache = cache;
                    container.ifPresent(it -> {
                        int width = MathHelper.ceil(it.getStoredAura() / (float) it.getMaxAura() * 80);
                        int x = window.getScaledWidth() / 2 - 173 - (mc.player.getHeldItemOffhand().isEmpty() ? 0 : 29);
                        int y = window.getScaledHeight() - 8;
                        int color = it.getAuraColor();

                        RenderSystem.color4f((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, 1);
                        mc.getTextureManager().bindTexture(OVERLAYS);
                        if (width < 80) AbstractGui.blit(x + width, y, width, 0, 80 - width, 6, 256, 256);
                        if (width > 0) AbstractGui.blit(x, y, 0, 6, width, 6, 256, 256);

                        float scale = 0.75F;
                        RenderSystem.scalef(scale, scale, scale);
                        String s = finalCache.getDisplayName().getFormattedText();
                        mc.fontRenderer.drawStringWithShadow(s, (x + 80) / scale - mc.fontRenderer.getStringWidth(s), (y - 7) / scale, color);

                        RenderSystem.color4f(1F, 1F, 1F, 1);
                        RenderSystem.popMatrix();
                    });
                }
            }
        }
    }

    @Nullable
    @Override
    public IAnimationStateMachine loadASM(ResourceLocation resourceLocation, ImmutableMap<String, ITimeValue> map) {
        return AnimationStateMachine.load(Minecraft.getInstance().getResourceManager(), resourceLocation, map);
    }

    @Override
    public <T extends TileEntity> void registerAnimationTESR(TileEntityType<T> type) {
        registerTESR(type, TileEntityRendererAnimation::new);
    }

    private <T extends TileEntity> void registerTESR(TileEntityType<T> type, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super TileEntity>> tesr) {
        ClientRegistry.bindTileEntityRenderer(type, tesr);
    }
}
