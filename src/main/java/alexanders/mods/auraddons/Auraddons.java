package alexanders.mods.auraddons;

import alexanders.mods.auraddons.aura.NetherDegradeEffect;
import alexanders.mods.auraddons.init.*;
import alexanders.mods.auraddons.init.generator.*;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import java.lang.reflect.Field;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static alexanders.mods.auraddons.Constants.MOD_ID;
import static alexanders.mods.auraddons.Constants.MOD_NAME;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Auraddons {
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static Auraddons instance;
    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static ConfigBuilder configBuilder;

    public boolean baublesLoaded;
    public boolean curiosLoaded;
    public Integer cacheBarLocation = 0;

    public Auraddons() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initialization);
        configBuilder = new ConfigBuilder();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if (ModBlocks.blockRegistry.isEmpty()) ModBlocks.init();
        logger.info("Registering blocks...");
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Block b : ModBlocks.blockRegistry) registry.register(b);
        logger.info("Registered {} blocks", ModBlocks.blockRegistry.size());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if (ModBlocks.blockRegistry.isEmpty()) ModBlocks.init();
        if (ModItems.itemRegistry.size() <= ModBlocks.blockRegistry.size()) ModItems.init();
        logger.info("Registering items...");
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Item b : ModItems.itemRegistry) registry.register(b);
        logger.info("Registered {} items", ModItems.itemRegistry.size());
    }

    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
        if (ModBlocks.tileTypeRegistry.isEmpty()) ModBlocks.init();
        logger.info("Registering tiles...");
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        for (TileEntityType<?> type : ModBlocks.tileTypeRegistry) registry.register(type);
        logger.info("Registered {} tiles", ModBlocks.tileTypeRegistry.size());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper ex = event.getExistingFileHelper();
        instance.curiosLoaded = true;
        final CuriosCompat compat = new CuriosCompat();
        MinecraftForge.EVENT_BUS.register(compat);
        FMLJavaModLoadingContext.get().getModEventBus().register(compat);
        CuriosCompat.init();

        generator.addProvider(new BlockStateGenerator(generator, ex));
        generator.addProvider(new ItemModelGenerator(generator, ex));
        generator.addProvider(new ItemTagGenerator(generator));
        generator.addProvider(new BlockLootGenerator(generator));
    }

    @SubscribeEvent
    public static void configLoadEvent(net.minecraftforge.fml.config.ModConfig.ModConfigEvent event) {
        ConfigBuilder.updateFields();
    }

    private void initialization(FMLCommonSetupEvent event) {
        preInit(event);
        init(event);
        postInit(event);
    }

    public void preInit(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        ModPackets.init();

        try {
            Class<?> modConfigClass = Class.forName("de.ellpeck.naturesaura.ModConfig");
            if (modConfigClass != null) {
                Field modConfigInstance = modConfigClass.getDeclaredField("instance");
                if (!modConfigInstance.isAccessible()) modConfigInstance.setAccessible(true);
                Object instance = modConfigInstance.get(null);
                if (instance != null) {
                    Field modConfigCacheBarLocation = modConfigClass.getDeclaredField("cacheBarLocation");
                    if (!modConfigCacheBarLocation.isAccessible()) modConfigCacheBarLocation.setAccessible(true);
                    Object cacheBarLocation = modConfigCacheBarLocation.get(instance);
                    if (cacheBarLocation instanceof ForgeConfigSpec.ConfigValue) {
                        final Object location = ((ForgeConfigSpec.ConfigValue) cacheBarLocation).get();
                        if (location instanceof Integer) {
                            this.cacheBarLocation = (Integer) location;
                        } else {
                            logger.warn(
                                    "Can't get cacheBarLocation from NaturesAura config, defaulting to 0. You may have to update Auraddons or Nature's Aura to get this configuration option to load.");
                        }
                    } else {
                        logger.warn(
                                "Can't get cacheBarLocation from NaturesAura config, defaulting to 0. You may have to update Auraddons or Nature's Aura to get this configuration option to load.");
                    }
                } else {
                    logger.warn(
                            "Can't get cacheBarLocation from NaturesAura config, defaulting to 0. You may have to update Auraddons or Nature's Aura to get this configuration option to load.");
                }
            } else {
                logger.warn(
                        "Can't get cacheBarLocation from NaturesAura config, defaulting to 0. You may have to update Auraddons or Nature's Aura to get this configuration option to load.");
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            logger.warn(
                    "Can't get cacheBarLocation from NaturesAura config, defaulting to 0. You may have to update Auraddons or Nature's Aura to get this configuration option to load.");
        }

        //        if (ModList.get().isLoaded("baubles")) {
        //            baublesLoaded = true;
        //            BaublesCompat.init();
        //            MinecraftForge.EVENT_BUS.register(new BaublesCompat());
        if (ModList.get().isLoaded("curios")) {
            curiosLoaded = true;
            final CuriosCompat compat = new CuriosCompat();
            MinecraftForge.EVENT_BUS.register(compat);
            FMLJavaModLoadingContext.get().getModEventBus().register(compat);
            CuriosCompat.init();
        }
        logger.info("Auraddons pre-initialized");
    }

    public void init(FMLCommonSetupEvent event) {
        Auraddons.proxy.registerAnimationTESR(ModBlocks.tileAutoWrath);
        if (ModConfig.general.enableNetherDegradeEffect) NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(NetherDegradeEffect.NAME, NetherDegradeEffect::new);
        ModRecipes.init();
    }

    public void postInit(FMLCommonSetupEvent event) {
        // Cleanup:
        //ModBlocks.blockRegistry = null;
        // ModItems.itemRegistry = null;
    }
}
