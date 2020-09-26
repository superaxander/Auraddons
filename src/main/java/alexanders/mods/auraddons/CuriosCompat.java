package alexanders.mods.auraddons;

import alexanders.mods.auraddons.init.ModItems;
import alexanders.mods.auraddons.init.generator.ItemTagGenerator;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class CuriosCompat {
    private static final Map<Item, SlotTypePreset> CURIOS_TYPES = ImmutableMap.<Item, SlotTypePreset>builder().put(ModItems.creativeAuraCache,
                                                                                                                   SlotTypePreset.BELT)
                                                                                                              .put(ModItems.dampeningFeather,
                                                                                                                   SlotTypePreset.RING)
                                                                                                              .build();

    public static void init() {
        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, CuriosCompat::attachCurios);
    }

    public static void addItemTags(ItemTagGenerator generator) {
        for (Map.Entry<Item, SlotTypePreset> entry : CURIOS_TYPES.entrySet()) {
            {
                ITag.INamedTag<Item> tag = ItemTags.createOptional(new ResourceLocation("curios", entry.getValue().getIdentifier()));
                generator.getBuilder(tag).add(entry.getKey());
            }
        }
    }

    @SubscribeEvent
    public void sendMessage(InterModEnqueueEvent event) {
        //        for (Item item : CURIOS_TYPES.keySet()) {
        //            InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder(item.));
        //        }
    }

    @SuppressWarnings("unchecked")
    public static void attachCurios(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (CURIOS_TYPES.containsKey(stack.getItem())) {
            event.addCapability(new ResourceLocation(MOD_ID, "bauble"), new ICapabilityProvider() {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
                    return capability == CuriosCapability.ITEM ? LazyOptional.of(() -> (T) new ICurio() {
                        @Override
                        public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                            stack.getItem().inventoryTick(stack, livingEntity.world, livingEntity, -1, false);
                        }

                        @Override
                        public boolean canRightClickEquip() {
                            return true;
                        }


                        @Override
                        public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
                            return true;
                        }
                    }) : LazyOptional.empty();
                }
            });
        }
    }
}
