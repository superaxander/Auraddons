package alexanders.mods.auraddons;

import alexanders.mods.auraddons.aura.NetherDegradeEffect;
import alexanders.mods.auraddons.init.*;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.ConfigBuilder;
import alexanders.mods.auraddons.init.generator.ItemModelGenerator;
import alexanders.mods.auraddons.init.generator.ItemTagGenerator;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
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
