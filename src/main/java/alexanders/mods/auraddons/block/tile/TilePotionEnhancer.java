package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.BrewingStandTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TilePotionEnhancer extends TileEntity {
    public static final ArrayList<TilePotionEnhancer> listenerList = new ArrayList<>();
    public static Field brewingItemStacksField;

    public TilePotionEnhancer() {
        super(ModBlocks.tilePotionEnhancer);
        synchronized (listenerList) {
            listenerList.add(this);
        }
    }

    @SubscribeEvent
    public static void handlePotionBrew(PotionBrewEvent.Post event) {
        NonNullList<ItemStack> stacks;
        try {
            final Field field = PotionBrewEvent.class.getDeclaredField("stacks");
            field.setAccessible(true);
            //noinspection unchecked
            stacks = (NonNullList<ItemStack>) field.get(event);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        //        NonNullList<ItemStack> stacks = ObfuscationReflectionHelper.getPrivateValue(PotionBrewEvent.class, event, "stacks");
        synchronized (TilePotionEnhancer.listenerList) {
            for (TilePotionEnhancer te : TilePotionEnhancer.listenerList) {
                te.enhancePotion(stacks);
            }
        }
    }

    @SubscribeEvent
    public static void handlePotionDrink(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof PlayerEntity) {
            ItemStack stack = event.getItem();
            if (stack.getItem().getClass() == PotionItem.class && stack.hasTag() && stack.getTag() != null && stack.getTag().getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
                for (EffectInstance effect : PotionUtils.getEffectsFromStack(stack)) {
                    ((PlayerEntity) event.getEntity()).addPotionEffect(
                            new EffectInstance(effect.getPotion(), effect.getDuration() * 2, effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles()));
                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void handleTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem().getClass() == PotionItem.class && stack.hasTag() && stack.getTag() != null && stack.getTag().getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
            List<ITextComponent> toolTip = event.getToolTip();
            int duration = PotionUtils.getEffectsFromStack(stack).get(0).getDuration() / 10;
            for (int i = 0; i < toolTip.size(); i++) {
                if (toolTip.get(i) instanceof StringTextComponent || toolTip.get(i) instanceof TranslationTextComponent) {
                    String t = toolTip.get(i).getString();
                    if (t.matches(".* \\([0-9]+:[0-9][0-9]\\)")) {
                        event.getToolTip().set(i, new StringTextComponent(
                                t.replaceFirst("\\([0-9]+:[0-9][0-9]\\)", String.format("%s(%d:%d0)", TextFormatting.DARK_PURPLE, duration / 60, duration % 60))));
                    }
                }
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        synchronized (listenerList) {
            listenerList.remove(this);
        }
    }


    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        synchronized (listenerList) {
            listenerList.remove(this);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        assert world != null;
        TileEntity te = world.getTileEntity(pos.up());
        if (te instanceof BrewingStandTileEntity) {
            final LazyOptional<T> capability1 = te.getCapability(capability, facing);
            if (capability1 != null) return capability1.cast();
            else return LazyOptional.empty();
        }
        return super.getCapability(capability, facing);
    }

    private boolean checkHash(int hash) {
        assert world != null;
        TileEntity te = world.getTileEntity(pos.up());
        if (te instanceof BrewingStandTileEntity) {
            if(brewingItemStacksField == null) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    brewingItemStacksField = BrewingStandTileEntity.class.getDeclaredField("field_145945_j");
                    brewingItemStacksField.setAccessible(true);
                } catch (NoSuchFieldException e) {
                    try {
                        brewingItemStacksField = BrewingStandTileEntity.class.getDeclaredField("brewingItemStacks");
                        brewingItemStacksField.setAccessible(true);
                    }catch (NoSuchFieldException e2) {
                        Auraddons.logger.error("There is a bug in the potion enhancer code. Please report this on the Auraddons issue tracker!",e2);
                    }
                }
            }
            if(brewingItemStacksField != null) {
                try {
                    //return Objects.hashCode(ObfuscationReflectionHelper.<NonNullList<ItemStack>, BrewingStandTileEntity>getPrivateValue(BrewingStandTileEntity.class, (BrewingStandTileEntity) te,"brewingItemStacks")) == hash;
                    return Objects.hashCode(brewingItemStacksField.get(te)) == hash;
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    Auraddons.logger.error(
                            "There is a bug in the potion enhancer code. Please report this on the Auraddons issue tracker!",
                            e);
                }
            }
        }
        return false;
    }

    public void enhancePotion(NonNullList<ItemStack> brewingItemStacks) {
        if (world != null && checkHash(Objects.hashCode(brewingItemStacks))) {
            if (brewingItemStacks != null) {
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = brewingItemStacks.get(i);
                    if (stack.getItem().getClass() == PotionItem.class) {
                        List<EffectInstance> effects = PotionUtils.getEffectsFromStack(stack);
                        if (effects.size() == 1) {
                            if (stack.getTag() != null && !stack.getTag().getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
                                stack.getTag().putBoolean(ModNames.TAG_DURATION_ENHANCED, true);
                                if (stack.getTag().contains("Lore", Constants.NBT.TAG_LIST)) {
                                    stack.getTag().getList("Lore", Constants.NBT.TAG_STRING).add(StringNBT.valueOf("Duration enhanced"));
                                } else {
                                    ListNBT list = new ListNBT();
                                    list.add(StringNBT.valueOf("Duration enhanced"));
                                    stack.getTag().put("Lore", list);
                                }
                                BlockPos spot = IAuraChunk.getHighestSpot(world, pos, 25, pos);
                                IAuraChunk.getAuraChunk(world, spot).drainAura(spot, ModConfig.aura.potionEnhancerCostPerLevel * (effects.get(0).getAmplifier() + 1));
                            }
                        }
                    }
                }
            }
        }
    }
}
