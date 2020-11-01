package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModPackets;
import alexanders.mods.auraddons.net.JumpPacket;
import alexanders.mods.auraddons.net.ParticlePacket;
import com.google.common.collect.ImmutableMap;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class TileAutoWrath extends TileEntity implements ITickableTileEntity {
    @Nullable
    private final IAnimationStateMachine asm;
    private final TimeValues.VariableValue steps = new TimeValues.VariableValue(1);
    public boolean jumping = false;
    private long doDamage = -1;
    private int cooldown = 200;

    public TileAutoWrath() {
        super(ModBlocks.tileAutoWrath);
        asm = Auraddons.proxy.loadASM(new ResourceLocation(MOD_ID, "asms/block/block_auto_wrath.json"),
                ImmutableMap.of("steps", steps));

    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder().withProperty(Properties.AnimationProperty).build();
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
    public void tick() {
        if (world != null && !world.isRemote) {
            if (doDamage != -1 && doDamage <= world.getGameTime()) {
                doDamage = -1;
                int range = 5;
                List<LivingEntity> mobs = world.getEntitiesWithinAABB(LivingEntity.class,
                        new AxisAlignedBB(pos.getX() + .5 - range, pos.getY() - 0.5, pos.getZ() + .5 - range,
                                pos.getX() + .5 + range,
                                pos.getY() + 0.5, pos.getZ() + .5 + range));
                BlockPos spot = IAuraChunk.getHighestSpot(world, pos, 25, pos);
                for (LivingEntity mob : mobs) {
                    if (!mob.isAlive()) continue;
                    if (pos.distanceSq(mob.getPositionVec(), true) > range * range) continue;
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
            } else if ((world.getGameTime() + 3) % 20 == 0) {
                boolean found = false;
                if (NaturesAuraAPI.TYPE_NETHER.isPresentInWorld(world)) {
                    BlockPos spot = IAuraChunk.getHighestSpot(world, pos, 25, pos);
                    IAuraChunk.getAuraChunk(world, spot).drainAura(spot, ModConfig.aura.autoWrathPulseCost);
                    found = true;
                } else {
                    TileEntity te = world.getTileEntity(pos.up());
                    if (te != null) {
                        final LazyOptional<IItemHandler> optHandler = te.getCapability(
                                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
                        if (optHandler.isPresent()) {
                            IItemHandler handler = optHandler.orElseThrow(IllegalStateException::new);
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
                                                ItemEntity e = new ItemEntity(world, pos.getX() + .5, pos.getY() + 1.5,
                                                        pos.getZ() + .5, glass);
                                                final Vector3d motion = e.getMotion();
                                                e.setMotion(
                                                        motion.x + world.rand.nextGaussian() * 0.007499999832361937D * 6,
                                                        motion.y + world.rand.nextGaussian() * 0.007499999832361937D * 6,
                                                        motion.z + world.rand.nextGaussian() * 0.007499999832361937D * 6);
                                                world.addEntity(e);
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
                    doDamage = world.getGameTime() + 20;
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
        if (!stack.hasTag()) return NaturesAuraAPI.TYPE_OTHER;
        assert stack.getTag() != null;
        String type = stack.getTag().getString("stored_type");
        if (type.isEmpty()) return NaturesAuraAPI.TYPE_OTHER;
        return NaturesAuraAPI.AURA_TYPES.get(new ResourceLocation(type));
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
        super.read(state, compound);
        doDamage = compound.getLong("doDamage");
        cooldown = compound.getInt("cooldown");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putLong("doDamage", doDamage);
        compound.putInt("cooldown", cooldown);
        return compound;
    }

    //    @Override
    //    public boolean hasFastRenderer() {
    //        return true;
    //    }


    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return LazyOptional.of(asm == null ? null : () -> (T) asm);
        }
        return super.getCapability(capability, side);
    }
}
