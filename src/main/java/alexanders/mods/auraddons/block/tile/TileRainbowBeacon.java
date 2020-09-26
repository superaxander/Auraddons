package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.init.ModBlocks;
import alexanders.mods.auraddons.init.ModConfig;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileRainbowBeacon extends TileEntity implements ITickableTileEntity {
    private static boolean reflectionFail;
    public final float[] colorMultiplier = new float[]{-1, -1, -1};
    private float hue = 0;

    public TileRainbowBeacon() {
        super(ModBlocks.tileRainbowBeacon);
    }

    @Override
    public void tick() {
        if (this.world != null && this.world.getGameTime() % 20L == 19L) { // Update the tick before the beacon
            world.getProfiler().startSection(() -> "RainbowBeaconUpdate");
            setHSV(hue = (hue + 1) % 360, 1f, 1f);
            if (ModConfig.general.smoothRainbowBeacon && !reflectionFail && this.world.getGameTime() % 80L != 79L) {
                TileEntity te = world.getTileEntity(pos.down());
                if (te instanceof BeaconTileEntity) {
                    //                    try {
                    //                        //ReflectionHelper.getPrivateValue(TileEntityBeacon.class, (TileEntityBeacon)te,"beamSegments", "field_174909_f");
                    //                        //TODO: Perhaps update *all* the beam segments manually? This would probably be less laggy!
                    //                        ObfuscationReflectionHelper.findMethod(BeaconTileEntity.class, "updateSegmentColors").invoke(te);
                    //                    } catch (ObfuscationReflectionHelper.UnableToFindMethodException e) {
                    //                        reflectionFail = true;
                    //                        Auraddons.logger
                    //                                .error("Unable to find TileEntityBeacon#updateSegmentColors with obf name func_146003_y. Auraddons may not be fully compatible with this version of minecraft");
                    //                    } catch (IllegalAccessException | InvocationTargetException e) {
                    //                        e.printStackTrace();
                    //                    }
                    ((BeaconTileEntity) te).tick();  
                }
            }
            world.getProfiler().endSection();
        }
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
        super.read(state, compound);
        hue = compound.getFloat("hue");
        setHSV(hue, 1f, 1f);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putFloat("hue", hue);
        return compound;
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        compound.putFloat("hue", hue);
        return compound;
    }

    @Override
    public void handleUpdateTag(@Nonnull BlockState state, @Nonnull CompoundNBT compound) {
        super.handleUpdateTag(state, compound);
        hue = compound.getFloat("hue");
        setHSV(hue, 1f, 1f);
    }

    /**
     * Converts HSV to RGB and sets the colorMultiplier variable
     *
     * @param hue        value between 0 and 360
     * @param saturation value between 0 and 1
     * @param value      value between 0 and 1
     */
    private void setHSV(float hue, float saturation, float value) {
        float c = value * saturation;
        float x = c * (1 - Math.abs((hue / 60) % 2 - 1));
        float m = value - c;

        if (hue < 60) {
            colorMultiplier[0] = c;
            colorMultiplier[1] = x;
            colorMultiplier[2] = 0;
        } else if (hue < 120) {
            colorMultiplier[0] = x;
            colorMultiplier[1] = c;
            colorMultiplier[2] = 0;
        } else if (hue < 180) {
            colorMultiplier[0] = 0;
            colorMultiplier[1] = c;
            colorMultiplier[2] = x;
        } else if (hue < 240) {
            colorMultiplier[0] = 0;
            colorMultiplier[1] = x;
            colorMultiplier[2] = c;
        } else if (hue < 300) {
            colorMultiplier[0] = x;
            colorMultiplier[1] = 0;
            colorMultiplier[2] = c;
        } else { // if (hue < 360)
            colorMultiplier[0] = c;
            colorMultiplier[1] = 0;
            colorMultiplier[2] = x;
        }

        colorMultiplier[0] += m;
        colorMultiplier[1] += m;
        colorMultiplier[2] += m;
    }
}
