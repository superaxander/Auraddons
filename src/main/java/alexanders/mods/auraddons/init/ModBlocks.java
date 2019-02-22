package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.block.*;
import alexanders.mods.auraddons.block.tile.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@GameRegistry.ObjectHolder(MOD_ID)
public final class ModBlocks {
    public static ArrayList<Block> blockRegistry = new ArrayList<>();

    @GameRegistry.ObjectHolder(ModNames.BLOCK_AUTO_WRATH)
    public static BlockAutoWrath autoWrath;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_ANCIENT_FENCE_GATE)
    public static Block ancientFenceGate;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_HARD_ICE)
    public static Block hardIce;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_DISRUPTION_CATALYST)
    public static Block disruptionCatalyst;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_ANCIENT_LADDER)
    public static Block ancientLadder;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_AURA_TRANSPORTER)
    public static Block auraTransporter;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_WITHER_PROOFER)
    public static Block witherProofer;

    public static void init() {
        if (ModConfig.blocks.enableAutoWrath) {
            add(autoWrath = new BlockAutoWrath());
            GameRegistry.registerTileEntity(TileAutoWrath.class, new ResourceLocation(MOD_ID, ModNames.TILE_AUTO_WRATH));
            Auraddons.proxy.registerAnimationTESR(TileAutoWrath.class);
        }
        if (ModConfig.blocks.enableAncientFence)
            add(new BlockFence(Material.WOOD, MapColor.WOOD) {{this.setSoundType(SoundType.WOOD);}}.setRegistryName(ModNames.BLOCK_ANCIENT_FENCE).setHardness(2.0F)
                        .setResistance(5.0F).setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_ANCIENT_FENCE).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableAncientFenceGate) {
            add(ancientFenceGate = new BlockFenceGate(BlockPlanks.EnumType.OAK) {{this.setSoundType(SoundType.WOOD);}}.setRegistryName(ModNames.BLOCK_ANCIENT_FENCE_GATE)
                    .setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_ANCIENT_FENCE_GATE).setHardness(2.0F).setResistance(5.0F).setCreativeTab(ModTabs.MAIN_TAB));
            Auraddons.proxy.ignoreState(ancientFenceGate, BlockFenceGate.POWERED);
        }
        if (ModConfig.blocks.enableInfusedStoneWall) add(new BlockWall(Blocks.COBBLESTONE) {
            @Override
            public void getSubBlocks(CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_INFUSED_STONE_WALL).setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_INFUSED_STONE_WALL).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableInfusedBrickWall) add(new BlockWall(Blocks.COBBLESTONE) {
            @Override
            public void getSubBlocks(CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_INFUSED_BRICK_WALL).setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_INFUSED_BRICK_WALL).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableGoldBrickWall) add(new BlockWall(Blocks.COBBLESTONE) {
            @Override
            public void getSubBlocks(CreativeTabs itemIn, @Nonnull NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_GOLD_BRICK_WALL).setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_GOLD_BRICK_WALL).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableAncientLadder) add(new BlockLadder() {{this.setSoundType(SoundType.LADDER);}}.setRegistryName(ModNames.BLOCK_ANCIENT_LADDER).setHardness(0.4F)
                                                              .setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_ANCIENT_LADDER).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableFreezer) {
            add(new BlockFreezer());
            GameRegistry.registerTileEntity(TileFreezer.class, new ResourceLocation(MOD_ID, ModNames.TILE_FREEZER));
        }
        if (ModConfig.blocks.enableHardIce)
            add(new BlockHardIce().setRegistryName(ModNames.BLOCK_HARD_ICE).setUnlocalizedName(MOD_ID + "." + ModNames.BLOCK_HARD_ICE).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enablePotionEnhancer) {
            add(new BlockPotionEnhancer());
            GameRegistry.registerTileEntity(TilePotionEnhancer.class, new ResourceLocation(MOD_ID, ModNames.TILE_POTION_ENHANCER));
        }
        if (ModConfig.blocks.enableDisruptionCatalyst) add(new BlockBase(ModNames.BLOCK_DISRUPTION_CATALYST, Material.ROCK));

        if (ModConfig.blocks.enableAuraTransporter) {
            add(new BlockAuraTransporter());
            GameRegistry.registerTileEntity(TileAuraTransporter.class, new ResourceLocation(MOD_ID, ModNames.TILE_AURA_TRANSPORTER));
        }

        if (ModConfig.blocks.enableWitherProofer) {
            add(new BlockWitherProofer());
            GameRegistry.registerTileEntity(TileWitherProofer.class, new ResourceLocation(MOD_ID, ModNames.TILE_WITHER_PROOFER));
        }

        if (ModConfig.blocks.enableRainbowBeacon) {
            add(new BlockRainbowBeacon());
            GameRegistry.registerTileEntity(TileRainbowBeacon.class, new ResourceLocation(MOD_ID, ModNames.TILE_RAINBOW_BEACON));
        }
    }

    private static void add(Block block) {
        blockRegistry.add(block);
        ModItems.itemRegistry.add(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
    }
}
