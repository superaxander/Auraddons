package alexanders.mods.auraddons;

import alexanders.mods.auraddons.aura.NetherDegradeEffect;
import alexanders.mods.auraddons.block.tile.TilePotionEnhancer;
import alexanders.mods.auraddons.init.*;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static alexanders.mods.auraddons.Constants.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPS)
@Mod.EventBusSubscriber(modid = MOD_ID)
public class Auraddons {
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    @Mod.Instance(owner = MOD_ID)
    public static Auraddons instance;
    @SidedProxy(modId = MOD_ID, clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static IProxy proxy;
    public boolean baublesLoaded;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModBlocks.init();
        ModItems.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        ModPackets.init();

        if (Loader.isModLoaded("baubles")) {
            baublesLoaded = true;
            BaublesCompat.init();
        }
        logger.info("Auraddons pre-initialized");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (ModConfig.general.enableNetherDegradeEffect) NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(NetherDegradeEffect.NAME, NetherDegradeEffect::new);
        ModRecipes.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Cleanup:
        ModBlocks.blockRegistry = null;
        ModItems.itemRegistry = null;
        //System.gc();
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        logger.info("Registering blocks...");
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block b : ModBlocks.blockRegistry) registry.register(b);
        logger.info("Registered {} blocks", ModBlocks.blockRegistry.size());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        logger.info("Registering items...");
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Item b : ModItems.itemRegistry) registry.register(b);
        logger.info("Registered {} items", ModItems.itemRegistry.size());
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        for (Item item : ModItems.itemRegistry) {
            // Items with special models might implement an interface later which we can check for here
            proxy.registerItemModel(item, 0, "inventory");
        }
    }

    @SubscribeEvent
    public void handlePotionBrew(PotionBrewEvent.Post event) {
        NonNullList<ItemStack> stacks = ReflectionHelper.getPrivateValue(PotionBrewEvent.class, event, "stacks");
        synchronized (TilePotionEnhancer.listenerList) {
            for (TilePotionEnhancer te : TilePotionEnhancer.listenerList) {
                te.enhancePotion(stacks);
            }
        }
    }

    @SubscribeEvent
    public void handlePotionDrink(LivingEntityUseItemEvent.Finish event) {
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
    public void handleTooltip(ItemTooltipEvent event) {
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
}
