package alexanders.mods.auraddons;

import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModItems;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.container.IAuraContainer;
import de.ellpeck.naturesaura.items.ItemAuraCache;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lwjgl.opengl.GL11;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class ClientProxy implements IProxy {
    public static final ResourceLocation OVERLAYS = new ResourceLocation(NaturesAuraAPI.MOD_ID, "textures/gui/overlays.png");
    private ItemStack creativeCache;
    private ItemStack normalCache;

    @Override
    public void postInit() {
        RenderTypeLookup.setRenderLayer(ModBlocks.ancientLadder, RenderType.getCutout());
    }


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            creativeCache = ItemStack.EMPTY;
            normalCache = ItemStack.EMPTY;
            Minecraft mc = Minecraft.getInstance();
            if (mc.world != null && mc.player != null && !mc.isGamePaused()) {
                if (Auraddons.instance.curiosLoaded) {
                    Optional<ItemStack> stack = CuriosApi.getCuriosHelper()
                                                         .findEquippedCurio(it -> it.getItem() instanceof ItemAuraCache, mc.player)
                                                         .map(ImmutableTriple::getRight);
                    stack.ifPresent(itemStack -> normalCache = itemStack);
                    if (normalCache.isEmpty()) {
                        stack = CuriosApi.getCuriosHelper()
                                         .findEquippedCurio(it -> it.getItem() == ModItems.creativeAuraCache, mc.player)
                                         .map(ImmutableTriple::getRight);
                        stack.ifPresent(itemStack -> creativeCache = itemStack);
                    }
                }

                if (normalCache.isEmpty()) {
                    for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                        ItemStack slot = mc.player.inventory.getStackInSlot(i);
                        if (!slot.isEmpty()) {
                            if (slot.getItem() instanceof ItemAuraCache) {
                                normalCache = slot;
                                break;
                            } else if (slot.getItem() == ModItems.creativeAuraCache) {
                                creativeCache = slot;
                            }
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            MainWindow window = event.getWindow();
            if (mc.player != null) {
                if (normalCache.isEmpty() && !creativeCache.isEmpty()) {
                    MatrixStack stack = event.getMatrixStack();
                    LazyOptional<IAuraContainer> container = creativeCache.getCapability(NaturesAuraAPI.capAuraContainer, null);
                    ItemStack finalCache = creativeCache;
                    container.ifPresent(it -> {
                        int width = MathHelper.ceil(it.getStoredAura() / (float) it.getMaxAura() * 80);
                        int x = window.getScaledWidth() / 2 + (Auraddons.instance.cacheBarLocation == 0 ?
                                -173 - (mc.player.getHeldItemOffhand().isEmpty() ? 0 : 29) : 93);
                        int y = window.getScaledHeight() - 8;
                        int color = it.getAuraColor();
                        GL11.glPushMatrix();
                        GL11.glColor4f((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, 1);
                        mc.getTextureManager().bindTexture(OVERLAYS);
                        if (width < 80) AbstractGui.blit(stack, x + width, y, width, 0, 80 - width, 6, 256, 256);
                        if (width > 0) AbstractGui.blit(stack, x, y, 0, 6, width, 6, 256, 256);

                        float scale = 0.75F;
                        GL11.glScalef(scale, scale, scale);
                        String s = finalCache.getDisplayName().getString();
                        mc.fontRenderer.drawStringWithShadow(stack, s, Auraddons.instance.cacheBarLocation == 1 ? x / scale :
                                (x + 80) / scale - mc.fontRenderer.getStringWidth(s), (y - 7) / scale, color);

                        GL11.glColor4f(1F, 1F, 1F, 1);
                        GL11.glPopMatrix();
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
        //registerTESR(type, TileEntityRendererAnimation::new);
    }

    private <T extends TileEntity> void registerTESR(TileEntityType<T> type, Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super TileEntity>> tesr) {
        ClientRegistry.bindTileEntityRenderer(type, tesr);
    }
}
