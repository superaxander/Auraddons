package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModPackets;
import alexanders.mods.auraddons.net.JumpPacket;
import alexanders.mods.auraddons.net.ParticlePacket;
import com.google.common.collect.ImmutableMap;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class TileAutoWrath extends TileEntity implements ITickable {
    @Nullable
    private final IAnimationStateMachine asm;
    private final TimeValues.VariableValue steps = new TimeValues.VariableValue(1);
    public boolean jumping = false;
    private long doDamage = -1;
    private int cooldown = 200;

    public TileAutoWrath() {
        asm = Auraddons.proxy.loadASM(new ResourceLocation(MOD_ID, "asms/block/block_auto_wrath_steve.json"), ImmutableMap.of("steps", steps));
    }

    public void startJumping() {
        if (asm != null && asm.currentState().equals("default")) {
            steps.setValue(.75f);
            asm.transition("jumping");
        }
    }

    public void stopJumping() {
        if (asm != null && asm.currentState().equals("jumping")) {
            asm.transition("default");
        }
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (doDamage != -1 && doDamage <= getWorld().getTotalWorldTime()) {
                doDamage = -1;
                int range = 5;
                List<EntityLiving> mobs = getWorld().getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.getX() + .5 - range, pos.getY() - 0.5, pos.getZ() + .5 - range,
                                                                                                                 pos.getX() + .5 + range, pos.getY() + 0.5,
                                                                                                                 pos.getZ() + .5 + range));
                BlockPos spot = IAuraChunk.getHighestSpot(world, pos, 25, pos);
                for (EntityLiving mob : mobs) {
                    if (mob.isDead) continue;
                    if (getDistanceSq(mob.posX, mob.posY, mob.posZ) > range * range) continue;
                    IAuraChunk.getAuraChunk(world, spot).drainAura(spot, ModConfig.aura.autoWrathMobDamageCost);
                    mob.attackEntityFrom(DamageSource.MAGIC, 4F);
                }
                ModPackets.sendAround(getWorld(), pos, 32, new ParticlePacket(ParticlePacket.Type.SHOCK_WAVE, pos));
                cooldown = 200;
                markDirty();
            } else if (doDamage != -1) {
                if (cooldown > 0) {
                    cooldown--;
                } else {
                    doDamage = -1;
                }
                markDirty();
            } else if ((world.getTotalWorldTime() + 3) % 20 == 0) {
                boolean found = false;
                if (NaturesAuraAPI.TYPE_NETHER.isPresentInWorld(world)) {
                    BlockPos spot = IAuraChunk.getHighestSpot(world, pos, 25, pos);
                    IAuraChunk.getAuraChunk(world, spot).drainAura(spot, ModConfig.aura.autoWrathPulseCost);
                    found = true;
                } else {
                    TileEntity te = getWorld().getTileEntity(pos.up());
                    if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                        if (handler != null) {
                            int slotAmount = handler.getSlots();
                            for (int i = 0; i < slotAmount; i++) {
                                ItemStack stack = handler.getStackInSlot(i);
                                ResourceLocation name = stack.getItem().getRegistryName();
                                if (name != null && name.equals(new ResourceLocation("naturesaura", "aura_bottle"))) {
                                    if (getType(stack) == NaturesAuraAPI.TYPE_NETHER) {
                                        if (handler.extractItem(i, 1, true).getCount() >= 1) {
                                            handler.extractItem(i, 1, false);
                                            ItemStack glass = new ItemStack(Items.GLASS_BOTTLE);
                                            if (!ItemHandlerHelper.insertItemStacked(handler, glass, false).isEmpty()) {
                                                EntityItem e = new EntityItem(world, pos.getX() + .5, pos.getY() + 1.5, pos.getZ() + .5, glass);
                                                e.motionX += world.rand.nextGaussian() * 0.007499999832361937D * 6;
                                                e.motionY += world.rand.nextGaussian() * 0.007499999832361937D * 6;
                                                e.motionZ += world.rand.nextGaussian() * 0.007499999832361937D * 6;
                                                world.spawnEntity(e);
                                            }
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (found) {
                    doDamage = getWorld().getTotalWorldTime() + 20;
                    if (!jumping) {
                        jumping = true;
                        ModPackets.sendTracking(world, pos, new JumpPacket(pos, true));
                    }
                } else {
                    jumping = false;
                    ModPackets.sendTracking(world, pos, new JumpPacket(pos, false));
                }
                markDirty();
            }
        }
    }

    private IAuraType getType(ItemStack stack) {
        if (!stack.hasTagCompound()) return NaturesAuraAPI.TYPE_OTHER;
        String type = stack.getTagCompound().getString("stored_type");
        if (type.isEmpty()) return NaturesAuraAPI.TYPE_OTHER;
        return NaturesAuraAPI.AURA_TYPES.get(new ResourceLocation(type));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        doDamage = compound.getLong("doDamage");
        cooldown = compound.getInteger("cooldown");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setLong("doDamage", doDamage);
        compound.setInteger("cooldown", cooldown);
        return compound;
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, side);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return super.getCapability(capability, side);
    }

    public void handleEvents(float time, Iterable<Event> pastEvents) {
    }
}
