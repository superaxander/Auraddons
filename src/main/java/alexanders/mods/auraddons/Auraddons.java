package alexanders.mods.auraddons;

import alexanders.mods.auraddons.aura.NetherDegradeEffect;
import alexanders.mods.auraddons.init.*;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
        NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(new ResourceLocation(MOD_ID, ModNames.NETHER_DEGRADE_EFFECT), NetherDegradeEffect::new);
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
}
