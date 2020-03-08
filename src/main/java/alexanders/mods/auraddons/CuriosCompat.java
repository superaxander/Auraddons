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
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CurioTags;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class CuriosCompat {
    private static final Map<Item, Tag<Item>> CURIOS_TYPES = ImmutableMap.<Item, Tag<Item>>builder().put(ModItems.creativeAuraCache, CurioTags.BELT)
            .put(ModItems.dampeningFeather, CurioTags.RING).build();

    public static void init() {

    }

    public static void addItemTags(ItemTagGenerator generator) {
        for (Item item : CURIOS_TYPES.keySet()) {
            generator.getBuilder(CURIOS_TYPES.get(item)).add(item);
        }
    }

    @SubscribeEvent
    public void sendMessage(InterModEnqueueEvent event) {
        for (Item item : CURIOS_TYPES.keySet()) {
            InterModComms.sendTo("curios", "register_type", () -> new CurioIMCMessage(CURIOS_TYPES.get(item).getId().getPath()));
        }
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void attachCurios(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (CURIOS_TYPES.containsKey(stack.getItem())) {
            event.addCapability(new ResourceLocation(MOD_ID, "bauble"), new ICapabilityProvider() {
                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
                    return capability == CuriosCapability.ITEM ? LazyOptional.of(() -> (T) new ICurio() {
                        public void onCurioTick(String identifier, int index, LivingEntity livingEntity) {
                            stack.getItem().inventoryTick(stack, livingEntity.world, livingEntity, -1, false);
                        }

                        public boolean canRightClickEquip() {
                            return true;
                        }

                        public boolean shouldSyncToTracking(String identifier, LivingEntity livingEntity) {
                            return true;
                        }
                    }) : LazyOptional.empty();
                }
            });
        }
    }
}
