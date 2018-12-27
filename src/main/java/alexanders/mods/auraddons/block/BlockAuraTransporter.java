package alexanders.mods.auraddons.block;

import alexanders.mods.auraddons.Constants;
import alexanders.mods.auraddons.block.tile.TileAuraTransporter;
import alexanders.mods.auraddons.init.ModConfig;
import alexanders.mods.auraddons.init.ModNames;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAuraTransporter extends BlockBase implements ITileEntityProvider {
    public static PropertyBool SENDING = PropertyBool.create("sending");

    public BlockAuraTransporter() {
        super(ModNames.BLOCK_AURA_TRANSPORTER, Material.ROCK);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return meta > 0 ? getDefaultState().withProperty(SENDING, true) : getDefaultState().withProperty(SENDING, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SENDING) ? 1 : 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileAuraTransporter) {
            BlockPos other = ((TileAuraTransporter) te).other;
            if (other != null && world.isBlockLoaded(other)) {
                IBlockState otherState = world.getBlockState(other);
                boolean thisMode = stateIn.getValue(SENDING);
                if (otherState.getBlock() == this && thisMode != otherState.getValue(SENDING)) {
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
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.updateRedstoneState(worldIn, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.updateRedstoneState(worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAuraTransporter) {
            if (!world.isRemote) {
                NBTTagCompound compound = player.getEntityData();
                if (!player.isSneaking() && compound.hasKey(ModNames.TAG_AURA_TRANSPORTER_POS)) {
                    BlockPos selectedPos = BlockPos.fromLong(compound.getLong(ModNames.TAG_AURA_TRANSPORTER_POS));
                    if (selectedPos.equals(pos)) {
                        player.sendStatusMessage(new TextComponentTranslation("info." + Constants.MOD_ID + ".same_position"), true);
                    } else if (pos.distanceSq(selectedPos) < ModConfig.general.auraTransporterRange * ModConfig.general.auraTransporterRange || !world.isBlockLoaded(selectedPos)) {
                        TileEntity other = world.getTileEntity(selectedPos);
                        if (other instanceof TileAuraTransporter) {
                            ((TileAuraTransporter) tile).other = selectedPos;
                            ((TileAuraTransporter) other).other = pos;
                            tile.markDirty();
                            other.markDirty();
                            player.sendStatusMessage(new TextComponentTranslation("info." + Constants.MOD_ID + ".connected"), true);
                        } else {
                            player.sendStatusMessage(new TextComponentTranslation("info." + Constants.MOD_ID + ".stored_pos_gone"), true);
                        }
                    } else {
                        player.sendStatusMessage(new TextComponentTranslation("info." + Constants.MOD_ID + ".too_far"), true);
                    }
                } else {
                    compound.setLong(ModNames.TAG_AURA_TRANSPORTER_POS, pos.toLong());
                    player.sendStatusMessage(new TextComponentTranslation("info." + Constants.MOD_ID + ".stored_pos"), true);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SENDING);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileAuraTransporter();
    }

    private void updateRedstoneState(@Nonnull World world, BlockPos pos) {
        if (!world.isRemote) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(SENDING, world.isBlockIndirectlyGettingPowered(pos) > 0));
        }
    }
}
