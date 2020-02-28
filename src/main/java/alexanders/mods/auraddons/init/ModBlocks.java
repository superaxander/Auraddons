package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.block.*;
import alexanders.mods.auraddons.block.tile.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@ObjectHolder(MOD_ID)
public final class ModBlocks {
    public static ArrayList<Block> blockRegistry = new ArrayList<>();

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
    public static TileEntityType<TilePotionEnhancer> tilePotionEnhancer;
    public static TileEntityType<TileRainbowBeacon> tileRainbowBeacon;

    public static void init() {
        if (ModConfig.blocks.enableAutoWrath) {
            add(autoWrath = new BlockAutoWrath());
            registerTileEntity(TileAutoWrath.class, new ResourceLocation(MOD_ID, ModNames.TILE_AUTO_WRATH));
            Auraddons.proxy.registerAnimationTESR(TileAutoWrath.class);
        }
        if (ModConfig.blocks.enableAncientFence)
            add(new FenceBlock(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0F, 5.0F)) {}.setRegistryName(ModNames.BLOCK_ANCIENT_FENCE));
        if (ModConfig.blocks.enableAncientFenceGate) {
            add(ancientFenceGate = new FenceGateBlock(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0F, 5.0F)) {}
                    .setRegistryName(ModNames.BLOCK_ANCIENT_FENCE_GATE));
            Auraddons.proxy.ignoreState(ancientFenceGate, FenceGateBlock.POWERED);
        }
        if (ModConfig.blocks.enableInfusedStoneWall) add(new WallBlock(Block.Properties.create(Material.ROCK)) {
            @Override
            public void fillItemGroup(ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_INFUSED_STONE_WALL));
        if (ModConfig.blocks.enableInfusedBrickWall) add(new WallBlock(Block.Properties.create(Material.ROCK)) {
            @Override
            public void fillItemGroup(ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_INFUSED_BRICK_WALL));
        if (ModConfig.blocks.enableGoldBrickWall) add(new WallBlock(Block.Properties.create(Material.ROCK)) {
            @Override
            public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_GOLD_BRICK_WALL));
        if (ModConfig.blocks.enableAncientLadder)
            add(new LadderBlock(Block.Properties.create(Material.WOOD).sound(SoundType.LADDER).hardnessAndResistance(0.4F)) {}.setRegistryName(ModNames.BLOCK_ANCIENT_LADDER));
        if (ModConfig.blocks.enablePotionEnhancer) {
            add(new BlockPotionEnhancer());
            registerTileEntity(TilePotionEnhancer.class, new ResourceLocation(MOD_ID, ModNames.TILE_POTION_ENHANCER));
        }
        if (ModConfig.blocks.enableDisruptionCatalyst) add(new BlockBase(ModNames.BLOCK_DISRUPTION_CATALYST, Material.ROCK));

        if (ModConfig.blocks.enableAuraTransporter) {
            add(new BlockAuraTransporter());
            registerTileEntity(TileAuraTransporter.class, new ResourceLocation(MOD_ID, ModNames.TILE_AURA_TRANSPORTER));
        }

        if (ModConfig.blocks.enableWitherProofer) {
            add(new BlockWitherProofer());
            registerTileEntity(TileWitherProofer.class, new ResourceLocation(MOD_ID, ModNames.TILE_WITHER_PROOFER));
        }

        if (ModConfig.blocks.enableRainbowBeacon) {
            add(new BlockRainbowBeacon());
            registerTileEntity(TileRainbowBeacon.class, new ResourceLocation(MOD_ID, ModNames.TILE_RAINBOW_BEACON));
        }
    }

    private static void add(Block block) {
        blockRegistry.add(block);
        ModItems.itemRegistry.add(new BlockItem(block, new Item.Properties().group(ModTabs.MAIN_TAB)).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
    }
}
