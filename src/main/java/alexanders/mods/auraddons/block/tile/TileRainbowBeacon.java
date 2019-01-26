package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.init.ModConfig;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TileRainbowBeacon extends TileEntity implements ITickable {
    private static boolean reflectionFail;
    public float[] colorMultiplier = new float[]{-1, -1, -1};
    private float hue = 0;

    @Override
    public void update() {
        if (this.world.getTotalWorldTime() % 20L == 19L) { // Update the tick before the beacon
            world.profiler.func_194340_a(() -> "RainbowBeaconUpdate");
            setHSV(hue = (hue + 1) % 360, 1f, 1f);
            if (ModConfig.general.smoothRainbowBeacon && !reflectionFail && this.world.getTotalWorldTime() % 80L != 79L) {
                TileEntity te = world.getTileEntity(pos.down());
                if (te instanceof TileEntityBeacon) {
                    try {
                        //ReflectionHelper.getPrivateValue(TileEntityBeacon.class, (TileEntityBeacon)te,"beamSegments", "field_174909_f");
                        //TODO: Perhaps update *all* the beam segments manually? This would probably be less laggy!
                        ReflectionHelper.findMethod(TileEntityBeacon.class, "updateSegmentColors", "func_146003_y").invoke(te);
                    } catch (ReflectionHelper.UnableToFindMethodException e) {
                        reflectionFail = true;
                        Auraddons.logger
                                .error("Unable to find TileEntityBeacon#updateSegmentColors with obf name func_146003_y. Auraddons may not be fully compatible with this version of minecraft");
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            world.profiler.endSection();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        hue = compound.getFloat("hue");
        setHSV(hue, 1f, 1f);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setFloat("hue", hue);
        return compound;
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setFloat("hue", hue);
        return compound;
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        hue = tag.getFloat("hue");
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
