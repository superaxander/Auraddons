package alexanders.mods.auraddons.init;

import alexanders.mods.auraddons.Auraddons;
import alexanders.mods.auraddons.block.BlockAutoWrath;
import alexanders.mods.auraddons.block.BlockFreezer;
import alexanders.mods.auraddons.block.BlockHardIce;
import alexanders.mods.auraddons.block.BlockPotionEnhancer;
import alexanders.mods.auraddons.block.tile.TileAutoWrath;
import alexanders.mods.auraddons.block.tile.TileFreezer;
import alexanders.mods.auraddons.block.tile.TilePotionEnhancer;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_ID;

@GameRegistry.ObjectHolder(MOD_ID)
public final class ModBlocks {
    @Nullable
    public static ArrayList<Block> blockRegistry = new ArrayList<>();

    @GameRegistry.ObjectHolder(ModNames.BLOCK_AUTO_WRATH)
    public static BlockAutoWrath autoWrath;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_ANCIENT_FENCE_GATE)
    public static Block ancientFenceGate;

    @GameRegistry.ObjectHolder(ModNames.BLOCK_HARD_ICE)
    public static Block hardIce;

    public static void init() {
        if (ModConfig.blocks.enableAutoWrath) {
            add(autoWrath = new BlockAutoWrath());
            GameRegistry.registerTileEntity(TileAutoWrath.class, new ResourceLocation(MOD_ID, ModNames.TILE_AUTO_WRATH));
            Auraddons.proxy.registerTESR(TileAutoWrath.class, new AnimationTESR<TileAutoWrath>() {
                @Override
                public void handleEvents(TileAutoWrath te, float time, Iterable<Event> pastEvents) {
                    te.handleEvents(time, pastEvents);
                }
            });
        }
        if (ModConfig.blocks.enableAncientFence)
            add(new BlockFence(Material.WOOD, MapColor.WOOD).setRegistryName(ModNames.BLOCK_ANCIENT_FENCE).setTranslationKey(MOD_ID + "." + ModNames.BLOCK_ANCIENT_FENCE)
                        .setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableAncientFenceGate) {
            add(ancientFenceGate = new BlockFenceGate(BlockPlanks.EnumType.OAK).setRegistryName(ModNames.BLOCK_ANCIENT_FENCE_GATE)
                    .setTranslationKey(MOD_ID + "." + ModNames.BLOCK_ANCIENT_FENCE_GATE).setCreativeTab(ModTabs.MAIN_TAB));
            Auraddons.proxy.ignoreState(ancientFenceGate, BlockFenceGate.POWERED);
        }
        if (ModConfig.blocks.enableInfusedStoneWall) add(new BlockWall(Blocks.COBBLESTONE) {
            @Override
            public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_INFUSED_STONE_WALL).setTranslationKey(MOD_ID + "." + ModNames.BLOCK_INFUSED_STONE_WALL).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableInfusedBrickWall) add(new BlockWall(Blocks.COBBLESTONE) {
            @Override
            public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
                items.add(new ItemStack(this));
            }
        }.setRegistryName(ModNames.BLOCK_INFUSED_BRICK_WALL).setTranslationKey(MOD_ID + "." + ModNames.BLOCK_INFUSED_BRICK_WALL).setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableAncientLadder)
            add(new BlockLadder() {}.setRegistryName(ModNames.BLOCK_ANCIENT_LADDER).setTranslationKey(MOD_ID + "." + ModNames.BLOCK_ANCIENT_LADDER)
                        .setCreativeTab(ModTabs.MAIN_TAB));
        if (ModConfig.blocks.enableFreezer) {
            add(new BlockFreezer());
            GameRegistry.registerTileEntity(TileFreezer.class, new ResourceLocation(MOD_ID, ModNames.TILE_FREEZER));
        }
        if (ModConfig.blocks.enableHardIce)
            add(new BlockHardIce().setRegistryName(ModNames.BLOCK_HARD_ICE).setTranslationKey(MOD_ID + "." + ModNames.BLOCK_HARD_ICE).setCreativeTab(ModTabs.MAIN_TAB));

        add(new BlockPotionEnhancer());
        GameRegistry.registerTileEntity(TilePotionEnhancer.class, new ResourceLocation(MOD_ID, ModNames.TILE_POTION_ENHANCER));
    }

    private static void add(Block block) {
        blockRegistry.add(block);
        ModItems.itemRegistry.add(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
    }
}
