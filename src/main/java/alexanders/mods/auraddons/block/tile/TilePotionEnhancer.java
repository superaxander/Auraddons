package alexanders.mods.auraddons.block.tile;

import alexanders.mods.auraddons.init.ModNames;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class TilePotionEnhancer extends TileEntity {
    public static final ArrayList<TilePotionEnhancer> listenerList = new ArrayList<>();

    public TilePotionEnhancer() {
        synchronized (listenerList) {
            listenerList.add(this);
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
                    if (stack.getTagCompound() != null && !stack.getTagCompound().getBoolean(ModNames.TAG_DURATION_ENHANCED)) {
                        stack.getTagCompound().setBoolean(ModNames.TAG_DURATION_ENHANCED, true);
                        if (stack.getTagCompound().hasKey("Lore", Constants.NBT.TAG_LIST)) {
                            stack.getTagCompound().getTagList("Lore", Constants.NBT.TAG_STRING).appendTag(new NBTTagString("Duration enhanced"));
                        } else {
                            NBTTagList list = new NBTTagList();
                            list.appendTag(new NBTTagString("Duration enhanced"));
                            stack.getTagCompound().setTag("Lore", list);
                        }
                    }
                }
            }
        }
    }
}
