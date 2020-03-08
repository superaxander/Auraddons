package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.block.tile.TileAuraTransporter;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.IStateProvider;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.render.IVisualizable;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockAuraTransporter extends BlockContainerBase implements IVisualizable, IStateProvider {
    public static BooleanProperty SENDING = BooleanProperty.create("sending");

    public BlockAuraTransporter() {
        super(ModNames.BLOCK_AURA_TRANSPORTER, Material.ROCK);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockState updatePostPlacement(@Nonnull BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        this.updateRedstoneState(worldIn, currentPos);
        return stateIn;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileAuraTransporter) {
            BlockPos other = ((TileAuraTransporter) te).other;
            if (other != null && world.isAreaLoaded(other, 0)) {
                BlockState otherState = world.getBlockState(other);
                boolean thisMode = stateIn.get(SENDING);
                if (otherState.getBlock() == this && thisMode != otherState.get(SENDING)) {
                    if (thisMode) {
                        for (int i = 0; i < 5; i++) {
                            NaturesAuraAPI.instance().spawnParticleStream(pos.getX() + 0.25F + rand.nextFloat() * 0.5F, pos.getY() + 0.25F + rand.nextFloat() * 0.5F,
                                                                          pos.getZ() + 0.25F + rand.nextFloat() * 0.5F, other.getX() + 0.25F + rand.nextFloat() * 0.5F,
                                                                          other.getY() + 0.25F + rand.nextFloat() * 0.5F, other.getZ() + 0.25F + rand.nextFloat() * 0.5F, .65f,
                                                                          0xCC3417, 1);
                        }
                    } else {
                        for (int i = 0; i < 5; i++) {
                            NaturesAuraAPI.instance().spawnParticleStream(other.getX() + 0.25F + rand.nextFloat() * 0.5F, other.getY() + 0.25F + rand.nextFloat() * 0.5F,
                                                                          other.getZ() + 0.25F + rand.nextFloat() * 0.5F, pos.getX() + 0.25F + rand.nextFloat() * 0.5F,
                                                                          pos.getY() + 0.25F + rand.nextFloat() * 0.5F, pos.getZ() + 0.25F + rand.nextFloat() * 0.5F, .65f,
                                                                          0xCC3417, 1);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        this.updateRedstoneState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult rayTraceResult) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAuraTransporter) {
            if (!world.isRemote) {
                CompoundNBT compound = player.getPersistentData();
                if (!player.isShiftKeyDown() && compound.contains(ModNames.TAG_AURA_TRANSPORTER_POS)) {
                    BlockPos selectedPos = BlockPos.fromLong(compound.getLong(ModNames.TAG_AURA_TRANSPORTER_POS));
                    if (selectedPos.equals(pos)) {
                        player.sendStatusMessage(new TranslationTextComponent("info." + Constants.MOD_ID + ".same_position"), true);
                    } else if (pos.distanceSq(selectedPos) < ModConfig.aura.auraTransporterRange * ModConfig.aura.auraTransporterRange || !world.isAreaLoaded(selectedPos, 0)) {
                        TileEntity other = world.getTileEntity(selectedPos);
                        if (other instanceof TileAuraTransporter) {
                            ((TileAuraTransporter) tile).other = selectedPos;
                            ((TileAuraTransporter) other).other = pos;
                            tile.markDirty();
                            other.markDirty();
                            player.sendStatusMessage(new TranslationTextComponent("info." + Constants.MOD_ID + ".connected"), true);
                        } else {
                            player.sendStatusMessage(new TranslationTextComponent("info." + Constants.MOD_ID + ".stored_pos_gone"), true);
                        }
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent("info." + Constants.MOD_ID + ".too_far"), true);
                    }
                } else {
                    compound.putLong(ModNames.TAG_AURA_TRANSPORTER_POS, pos.toLong());
                    player.sendStatusMessage(new TranslationTextComponent("info." + Constants.MOD_ID + ".stored_pos"), true);
                }
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        super.fillStateContainer(container);
        container.add(SENDING);
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        this.updateRedstoneState(world, pos);
    }

    @Override
    public int tickRate(IWorldReader worldIn) {
        return 4;
    }


    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        updateRedstoneState(world, pos);
    }

    @Override
    public void provideState(BlockStateGenerator generator) {
        //        generator.models().cubeBottomTop(ModNames.BLOCK_AURA_TRANSPORTER+"#sending=false", generator.modLoc("blocks/" + ModNames.BLOCK_AURA_TRANSPORTER),
        //                                         generator.modLoc("blocks/" + ModNames.BLOCK_AURA_TRANSPORTER + "_top"),
        //                                         generator.modLoc("blocks/" + ModNames.BLOCK_AURA_TRANSPORTER + "_top"));
        //        generator.models().cubeBottomTop(ModNames.BLOCK_AURA_TRANSPORTER+"#sending=true", generator.modLoc("blocks/" + ModNames.BLOCK_AURA_TRANSPORTER+"_sending"),
        //                                         generator.modLoc("blocks/" + ModNames.BLOCK_AURA_TRANSPORTER + "_top"),
        //                                         generator.modLoc("blocks/" + ModNames.BLOCK_AURA_TRANSPORTER + "_top"));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
        return new TileAuraTransporter();
    }

    private void updateRedstoneState(@Nonnull IWorldReader world, BlockPos pos) {
        if (!world.isRemote() && world instanceof World) {
            final World wrld = (World) world;
            wrld.setBlockState(pos, world.getBlockState(pos).with(SENDING, wrld.getRedstonePowerFromNeighbors(pos) > 0), 0);
            wrld.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
            if (wrld instanceof ServerWorld) {
                ((ServerWorld) wrld).getChunkProvider().markBlockChanged(pos);
            }
        }
    }

    @Override
    public AxisAlignedBB getVisualizationBounds(World world, BlockPos pos) {
        return new AxisAlignedBB(pos).grow(ModConfig.aura.auraTransporterRange);
    }

    @Override
    public int getVisualizationColor(World world, BlockPos pos) {
        return 0x3AC5FF;
    }
}
