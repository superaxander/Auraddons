package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class TilePotionEnhancer extends TileEntity {
    public static final ArrayList<TilePotionEnhancer> listenerList = new ArrayList<>();

    public TilePotionEnhancer() {
        synchronized (listenerList) {
            listenerList.add(this);
        }
    }

    @SubscribeEvent
    public static void handlePotionBrew(PotionBrewEvent.Post event) {
        NonNullList<ItemStack> stacks = ReflectionHelper.getPrivateValue(PotionBrewEvent.class, event, "stacks");
        synchronized (TilePotionEnhancer.listenerList) {
            for (TilePotionEnhancer te : TilePotionEnhancer.listenerList) {
                te.enhancePotion(stacks);
            }
        }
    }

    @SubscribeEvent
    public static void handlePotionDrink(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof EntityPlayer) {
            ItemStack stack = event.getItem();
            if (stack.getItem() instanceof ItemPotion && stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound()
                    .getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
                for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
                    ((EntityPlayer) event.getEntity()).addPotionEffect(
                            new PotionEffect(effect.getPotion(), effect.getDuration() * 2, effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void handleTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ItemPotion && stack.hasTagCompound() && stack.getTagCompound() != null && stack.getTagCompound()
                .getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
            List<String> toolTip = event.getToolTip();
            int duration = PotionUtils.getEffectsFromStack(stack).get(0).getDuration() / 10;
            for (int i = 0; i < toolTip.size(); i++) {
                String t = toolTip.get(i);
                if (t.matches(".* \\([0-9]+:[0-9][0-9]\\)")) {
                    event.getToolTip().set(i, t.replaceFirst("\\([0-9]+:[0-9][0-9]\\)", String.format("%s(%d:%d0)", TextFormatting.DARK_PURPLE, duration / 60, duration % 60)));
                }
            }
        }
    }

    private boolean checkHash(int hash) {
        TileEntity te = world.getTileEntity(pos.up());
        if (te instanceof TileEntityBrewingStand) {
            return Objects.hashCode(
                    ReflectionHelper.<NonNullList<ItemStack>, TileEntityBrewingStand>getPrivateValue(TileEntityBrewingStand.class, (TileEntityBrewingStand) te, "field_145945_j",
                                                                                                     "brewingItemStacks")) == hash;
        }
        return false;
    }

    public void enhancePotion(NonNullList<ItemStack> brewingItemStacks) {
        if (checkHash(Objects.hashCode(brewingItemStacks))) {
            if (brewingItemStacks != null) {
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = brewingItemStacks.get(i);
                    if (stack.getItem() instanceof ItemPotion) {
                        List<PotionEffect> effects = PotionUtils.getEffectsFromStack(stack);
                        if (effects.size() == 1) {
                            if (stack.getTagCompound() != null && !stack.getTagCompound().getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
                                stack.getTagCompound().setBoolean(ModNames.TAG_DURATION_ENHANCED, true);
                                if (stack.getTagCompound().hasKey("Lore", Constants.NBT.TAG_LIST)) {
                                    stack.getTagCompound().getTagList("Lore", Constants.NBT.TAG_STRING).appendTag(new NBTTagString("Duration enhanced"));
                                } else {
                                    NBTTagList list = new NBTTagList();
                                    list.appendTag(new NBTTagString("Duration enhanced"));
                                    stack.getTagCompound().setTag("Lore", list);
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

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        TileEntity te = world.getTileEntity(pos.up());
        if (te instanceof TileEntityBrewingStand) return te.hasCapability(capability, facing);
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        TileEntity te = world.getTileEntity(pos.up());
        if (te instanceof TileEntityBrewingStand) return te.getCapability(capability, facing);
        return super.getCapability(capability, facing);
    }
}
