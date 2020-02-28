package alexanders.mods.auraddons;

import alexanders.mods.auraddons.aura.NetherDegradeEffect;
import alexanders.mods.auraddons.init.*;
import alexanders.mods.auraddons.init.generator.BlockStateGenerator;
import alexanders.mods.auraddons.init.generator.ItemModelGenerator;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
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
@Mod.EventBusSubscriber(modid = MOD_ID)
public class Auraddons {
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static Auraddons instance;
    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    ;
    public boolean baublesLoaded;

    public Auraddons() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initialization);
    }

    private void initialization(FMLCommonSetupEvent event) {
        preInit(event);
        init(event);
        postInit(event);
    }

    public void preInit(FMLCommonSetupEvent event) {
        ModBlocks.init();
        ModItems.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
        ModPackets.init();


        //        if (ModList.get().isLoaded("baubles")) {
        //            baublesLoaded = true;
        //            BaublesCompat.init();
        //            MinecraftForge.EVENT_BUS.register(new BaublesCompat());
        //        } TODO: CURIOS
        logger.info("Auraddons pre-initialized");
    }

    public void init(FMLCommonSetupEvent event) {
        if (ModConfig.general.enableNetherDegradeEffect) NaturesAuraAPI.DRAIN_SPOT_EFFECTS.put(NetherDegradeEffect.NAME, NetherDegradeEffect::new);
        ModRecipes.init();
    }

    public void postInit(FMLCommonSetupEvent event) {
        // Cleanup:
        ModBlocks.blockRegistry = null;
        ModItems.itemRegistry = null;
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
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper ex = event.getExistingFileHelper();
     
        generator.addProvider(new BlockStateGenerator(generator, ex));
        generator.addProvider(new ItemModelGenerator(generator, ex));
    }
}
