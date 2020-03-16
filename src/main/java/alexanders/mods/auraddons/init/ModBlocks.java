package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.block.*;
import alexanders.mods.auraddons.block.tile.*;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@ObjectHolder(MOD_ID)
public final class ModBlocks {
    public static ArrayList<Block> blockRegistry = new ArrayList<>();
    public static ArrayList<TileEntityType<?>> tileTypeRegistry = new ArrayList<>();

    @ObjectHolder("naturesaura:gold_brick")
    public static Block goldBrick;

    @ObjectHolder("naturesaura:gold_nether_brick")
    public static Block goldNetherBrick;

    @ObjectHolder(ModNames.BLOCK_AUTO_WRATH)
    public static BlockAutoWrath autoWrath;

    public static TileEntityType<TileAutoWrath> tileAutoWrath;

    @ObjectHolder(ModNames.BLOCK_ANCIENT_FENCE_GATE)
    public static Block ancientFenceGate;

    @ObjectHolder(ModNames.BLOCK_DISRUPTION_CATALYST)
    public static Block disruptionCatalyst;

    @ObjectHolder(ModNames.BLOCK_ANCIENT_LADDER)
    public static Block ancientLadder;

    @ObjectHolder(ModNames.BLOCK_AURA_TRANSPORTER)
    public static Block auraTransporter;

    public static TileEntityType<TileAuraTransporter> tileAuraTransporter;

    @ObjectHolder(ModNames.BLOCK_WITHER_PROOFER)
    public static Block witherProofer;

    public static TileEntityType<TileWitherProofer> tileWitherProofer;

    @ObjectHolder(ModNames.BLOCK_RAINBOW_BEACON)
    public static Block rainbowBeacon;

    public static TileEntityType<TileRainbowBeacon> tileRainbowBeacon;

    @ObjectHolder(ModNames.BLOCK_POTION_ENHANCER)
    public static Block potionEnhancer;

    public static TileEntityType<TilePotionEnhancer> tilePotionEnhancer;

    public static void init() {

        add(autoWrath = new BlockAutoWrath());
        //noinspection ConstantConditions
        tileTypeRegistry.add((tileAutoWrath = TileEntityType.Builder.create(TileAutoWrath::new, autoWrath).build(null)).setRegistryName(ModNames.TILE_AUTO_WRATH));


        add(new FenceBlock(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0F, 5.0F)) {}.setRegistryName(ModNames.BLOCK_ANCIENT_FENCE));

        add(ancientFenceGate = new FenceGateBlock(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0F, 5.0F)) {}
                .setRegistryName(ModNames.BLOCK_ANCIENT_FENCE_GATE));

        add(new WallBlock(Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_INFUSED_STONE_WALL));
        add(new WallBlock(Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_INFUSED_BRICK_WALL));
        add(new WallBlock(Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_GOLD_BRICK_WALL));
        add(new StairsBlock(() -> goldBrick.getDefaultState(), Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_GOLD_BRICK_STAIRS));
        add(new SlabBlock(Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_GOLD_BRICK_SLAB));
        add(new WallBlock(Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_GOLD_NETHER_BRICK_WALL));
        add(new StairsBlock(() -> goldNetherBrick.getDefaultState(), Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_GOLD_NETHER_BRICK_STAIRS));
        add(new SlabBlock(Block.Properties.create(Material.ROCK)).setRegistryName(ModNames.BLOCK_GOLD_NETHER_BRICK_SLAB));

        add(new LadderBlock(Block.Properties.create(Material.WOOD).sound(SoundType.LADDER).hardnessAndResistance(0.4F)) {}.setRegistryName(ModNames.BLOCK_ANCIENT_LADDER));

        add(potionEnhancer = new BlockPotionEnhancer());
        //noinspection ConstantConditions
        tileTypeRegistry
                .add((tilePotionEnhancer = TileEntityType.Builder.create(TilePotionEnhancer::new, potionEnhancer).build(null)).setRegistryName(ModNames.TILE_POTION_ENHANCER));

        add(new BlockBase(ModNames.BLOCK_DISRUPTION_CATALYST, Material.ROCK));


        add(auraTransporter = new BlockAuraTransporter());
        //noinspection ConstantConditions
        tileTypeRegistry
                .add((tileAuraTransporter = TileEntityType.Builder.create(TileAuraTransporter::new, auraTransporter).build(null)).setRegistryName(ModNames.TILE_AURA_TRANSPORTER));


        add(witherProofer = new BlockWitherProofer());
        //noinspection ConstantConditions
        tileTypeRegistry.add((tileWitherProofer = TileEntityType.Builder.create(TileWitherProofer::new, witherProofer).build(null)).setRegistryName(ModNames.TILE_WITHER_PROOFER));


        add(rainbowBeacon = new BlockRainbowBeacon());
        //noinspection ConstantConditions
        tileTypeRegistry.add((tileRainbowBeacon = TileEntityType.Builder.create(TileRainbowBeacon::new, rainbowBeacon).build(null)).setRegistryName(ModNames.TILE_RAINBOW_BEACON));

    }

    private static void add(Block block) {
        blockRegistry.add(block);
        ModItems.itemRegistry.add(new BlockItem(block, new Item.Properties().group(ModTabs.MAIN_TAB)).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
    }
}
