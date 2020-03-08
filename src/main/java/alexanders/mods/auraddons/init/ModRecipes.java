package alexanders.mods.auraddons.init;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.api.recipes.AltarRecipe;
import de.ellpeck.naturesaura.api.recipes.OfferingRecipe;
import de.ellpeck.naturesaura.api.recipes.TreeRitualRecipe;
import de.ellpeck.naturesaura.api.recipes.ing.AmountIngredient;
import de.ellpeck.naturesaura.api.recipes.ing.NBTIngredient;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class ModRecipes {
    @ObjectHolder("naturesaura:ancient_sapling")
    public static Block ancientSapling;

    @ObjectHolder("naturesaura:ancient_log")
    public static Item ancientLog;

    @ObjectHolder("naturesaura:ancient_bark")
    public static Item ancientBark;

    @ObjectHolder("naturesaura:ancient_planks")
    public static Item ancientPlanks;

    @ObjectHolder("naturesaura:ancient_stick")
    public static Item ancientStick;

    @ObjectHolder("naturesaura:shockwave_creator")
    public static Item shockwaveCreator;

    @ObjectHolder("naturesaura:aura_bottle")
    public static Item auraBottle;

    @ObjectHolder("naturesaura:infused_iron")
    public static Item infusedIron;

    @ObjectHolder("naturesaura:infused_stone")
    public static Item infusedStone;

    @ObjectHolder("naturesaura:calling_spirit")
    public static Item callingSpirit;

    @ObjectHolder("naturesaura:sky_ingot")
    public static Item skyIngot;

    @ObjectHolder("naturesaura:gold_leaf")
    public static Item goldLeaf;

    @ObjectHolder("naturesaura:birth_spirit")
    public static Item birthingSpirit;

    public static void init() {
        new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_AUTO_WRATH), Ingredient.fromStacks(new ItemStack(Blocks.ACACIA_SAPLING)),
                             new ItemStack(ModBlocks.autoWrath), ModConfig.general.autoWrathRitualTime, Ingredient.fromItems(infusedStone),
                             NBTIngredient.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_NETHER)), Ingredient.fromItems(infusedStone),
                             Ingredient.fromItems(infusedIron), Ingredient.fromItems(shockwaveCreator), Ingredient.fromItems(infusedIron),
                             NBTIngredient.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_NETHER)), Ingredient.fromItems(infusedStone)).register();
        new OfferingRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SKY_FEATHER), new AmountIngredient(new ItemStack(Items.FEATHER, 1)), Ingredient.fromItems(callingSpirit),
                           new ItemStack(ModItems.skyFeather)).register();

        new OfferingRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SKY_BOTTLE), new AmountIngredient(setType(new ItemStack(auraBottle, 3), NaturesAuraAPI.TYPE_OVERWORLD)),
                           Ingredient.fromItems(callingSpirit), new ItemStack(ModItems.skyBottle)).register();


        new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_AURA_TRANSPORTER), Ingredient.fromStacks(new ItemStack(ancientSapling)),
                             new ItemStack(ModBlocks.auraTransporter), ModConfig.general.auraTransporterRitualTime, Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)),
                             Ingredient.fromItems(skyIngot), Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)),
                             NBTIngredient.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_END)), Ingredient.fromItems(skyIngot),
                             Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)), Ingredient.fromItems(skyIngot), Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)))
                .register();


        new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_DISRUPTION_CATALYST), Ingredient.fromStacks(new ItemStack(Items.BIRCH_SAPLING)),
                             new ItemStack(ModBlocks.disruptionCatalyst), ModConfig.general.disruptionCatalystRitualTime, Ingredient.fromStacks(new ItemStack(Blocks.TNT)),
                             Ingredient.fromStacks(new ItemStack(infusedStone)), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(skyIngot),
                             Ingredient.fromItems(goldLeaf), Ingredient.fromStacks(new ItemStack(Blocks.TNT))).register();

        Ingredient destructionCatalyst = Ingredient.fromStacks(new ItemStack(ModBlocks.disruptionCatalyst));
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_QUARTZ_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.QUARTZ_BLOCK)), new ItemStack(Items.QUARTZ, 4),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 100).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_QUARTZ_DISRUPTION_CHISELED), Ingredient.fromStacks(new ItemStack(Blocks.CHISELED_QUARTZ_BLOCK)),
                        new ItemStack(Items.QUARTZ, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 100).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_QUARTZ_DISRUPTION_PILLAR), Ingredient.fromStacks(new ItemStack(Blocks.QUARTZ_PILLAR)),
                        new ItemStack(Items.QUARTZ, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 100).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_GRAVEL_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)), new ItemStack(Items.FLINT, 2),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        //new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_STONE_BRICKS_DISRUPTION), Ingredient
        //        .fromStacks(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.MOSSY_META),
        //                    new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CRACKED_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META)),
        //                new ItemStack(Blocks.STONE, 4), destructionCatalyst, 25, 20).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_acacia"), Ingredient.fromStacks(new ItemStack(Blocks.ACACIA_LOG)),
                        new ItemStack(Blocks.ACACIA_PLANKS, 5), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_birch"), Ingredient.fromStacks(new ItemStack(Blocks.BIRCH_LOG)),
                        new ItemStack(Blocks.BIRCH_PLANKS, 5), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_dark_oak"), Ingredient.fromStacks(new ItemStack(Blocks.DARK_OAK_LOG)),
                        new ItemStack(Blocks.DARK_OAK_PLANKS, 5), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_jungle"), Ingredient.fromStacks(new ItemStack(Blocks.JUNGLE_LOG)),
                        new ItemStack(Blocks.JUNGLE_PLANKS, 5), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_oak"), Ingredient.fromStacks(new ItemStack(Blocks.OAK_LOG)),
                        new ItemStack(Blocks.OAK_PLANKS, 5), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_spruce"), Ingredient.fromStacks(new ItemStack(Blocks.SPRUCE_LOG)),
                        new ItemStack(Blocks.SPRUCE_PLANKS, 5), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION_ANCIENT_LOG), Ingredient.fromItems(ancientLog), new ItemStack(ancientPlanks, 4),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION_ANCIENT_BARK), Ingredient.fromItems(ancientBark), new ItemStack(ancientPlanks, 4),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 150).register();


        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_cyan"), Ingredient.fromStacks(new ItemStack(Blocks.CYAN_CONCRETE)),
                        new ItemStack(Blocks.CYAN_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_black"), Ingredient.fromStacks(new ItemStack(Blocks.BLACK_CONCRETE)),
                        new ItemStack(Blocks.BLACK_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_blue"), Ingredient.fromStacks(new ItemStack(Blocks.BLUE_CONCRETE)),
                        new ItemStack(Blocks.BLUE_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_brown"), Ingredient.fromStacks(new ItemStack(Blocks.BROWN_CONCRETE)),
                        new ItemStack(Blocks.BROWN_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_gray"), Ingredient.fromStacks(new ItemStack(Blocks.GRAY_CONCRETE)),
                        new ItemStack(Blocks.GRAY_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_green"), Ingredient.fromStacks(new ItemStack(Blocks.GREEN_CONCRETE)),
                        new ItemStack(Blocks.GREEN_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_light_blue"), Ingredient.fromStacks(new ItemStack(Blocks.LIGHT_BLUE_CONCRETE)),
                        new ItemStack(Blocks.LIGHT_BLUE_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_light_gray"), Ingredient.fromStacks(new ItemStack(Blocks.LIGHT_GRAY_CONCRETE)),
                        new ItemStack(Blocks.LIGHT_GRAY_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_lime"), Ingredient.fromStacks(new ItemStack(Blocks.LIME_CONCRETE)),
                        new ItemStack(Blocks.LIME_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_magenta"), Ingredient.fromStacks(new ItemStack(Blocks.MAGENTA_CONCRETE)),
                        new ItemStack(Blocks.MAGENTA_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_orange"), Ingredient.fromStacks(new ItemStack(Blocks.ORANGE_CONCRETE)),
                        new ItemStack(Blocks.ORANGE_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_pink"), Ingredient.fromStacks(new ItemStack(Blocks.PINK_CONCRETE)),
                        new ItemStack(Blocks.PINK_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_purple"), Ingredient.fromStacks(new ItemStack(Blocks.PURPLE_CONCRETE)),
                        new ItemStack(Blocks.PURPLE_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_red"), Ingredient.fromStacks(new ItemStack(Blocks.RED_CONCRETE)),
                        new ItemStack(Blocks.RED_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_white"), Ingredient.fromStacks(new ItemStack(Blocks.WHITE_CONCRETE)),
                        new ItemStack(Blocks.WHITE_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION + "_yellow"), Ingredient.fromStacks(new ItemStack(Blocks.YELLOW_CONCRETE)),
                        new ItemStack(Blocks.YELLOW_CONCRETE_POWDER), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();


        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.SANDSTONE)), new ItemStack(Blocks.SAND, 4),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION_CHISELED), Ingredient.fromStacks(new ItemStack(Blocks.CHISELED_SANDSTONE)),
                        new ItemStack(Blocks.SAND, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION_SMOOTH), Ingredient.fromStacks(new ItemStack(Blocks.SMOOTH_SANDSTONE)),
                        new ItemStack(Blocks.SAND, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION_RED), Ingredient.fromStacks(new ItemStack(Blocks.RED_SANDSTONE)),
                        new ItemStack(Blocks.RED_SAND, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION_RED_CHISELED), Ingredient.fromStacks(new ItemStack(Blocks.CHISELED_RED_SANDSTONE)),
                        new ItemStack(Blocks.SAND, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION_RED_SMOOTH), Ingredient.fromStacks(new ItemStack(Blocks.SMOOTH_RED_SANDSTONE)),
                        new ItemStack(Blocks.RED_SAND, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_GLOWSTONE_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.GLOWSTONE)),
                        new ItemStack(Items.GLOWSTONE_DUST, 4), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_LADDER_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.LADDER)), new ItemStack(Items.STICK, 7),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();
        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_LADDER_DISRUPTION_ANCIENT), Ingredient.fromStacks(new ItemStack(ModBlocks.ancientLadder)),
                        new ItemStack(ancientStick, 7), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_BREAD_DISRUPTION), Ingredient.fromStacks(new ItemStack(Items.BREAD)), new ItemStack(Items.WHEAT, 3),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SUGAR_CANE_DISRUPTION), Ingredient.fromStacks(new ItemStack(Items.SUGAR_CANE)), new ItemStack(Items.SUGAR, 2),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_MELON_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.MELON)), new ItemStack(Items.MELON, 9),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 100, 50).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_JUKEBOX_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.JUKEBOX)), new ItemStack(Items.DIAMOND, 1),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 100).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_ENDER_CHEST_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.ENDER_CHEST)),
                        new ItemStack(Items.ENDER_EYE, 1), NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 150, 100).register();

        new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_BEACON_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.BEACON)), new ItemStack(Items.NETHER_STAR, 1),
                        NaturesAuraAPI.TYPE_OVERWORLD, destructionCatalyst, 200, 100).register();


        final Ingredient skullIngredient1 = Ingredient.fromStacks(new ItemStack(Items.WITHER_SKELETON_SKULL));
        final Ingredient skullIngredient2 = Ingredient.fromStacks(new ItemStack(Items.WITHER_SKELETON_SKULL));
        final Ingredient skullIngredient3 = Ingredient.fromStacks(new ItemStack(Items.WITHER_SKELETON_SKULL));

        new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WITHER_PROOFER), Ingredient.fromStacks(new ItemStack(Items.BIRCH_SAPLING)),
                             new ItemStack(ModBlocks.witherProofer), ModConfig.general.witherProoferRitualTime, skullIngredient1, skullIngredient1, skullIngredient1,
                             Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN)), Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN)), Ingredient.fromItems(skyIngot),
                             Ingredient.fromItems(skyIngot), Ingredient.fromItems(Items.NETHER_STAR)).register();

        //        new AnimalSpawnerRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WITHER_SPAWNER + "_1"), EntityType.WITHER, ModConfig.aura.witherSpawnerCost,
        //                                ModConfig.general.witherSpawnerTime, skullIngredient1, skullIngredient1, skullIngredient1, Ingredient.fromItems(birthingSpirit)).register();
        //        new AnimalSpawnerRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WITHER_SPAWNER + "_2"), EntityType.WITHER, ModConfig.aura.witherSpawnerCost,
        //                                ModConfig.general.witherSpawnerTime, skullIngredient2, skullIngredient1, Ingredient.fromItems(birthingSpirit)).register();
        //        new AnimalSpawnerRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WITHER_SPAWNER + "_3"), EntityType.WITHER, ModConfig.aura.witherSpawnerCost,
        //                                ModConfig.general.witherSpawnerTime, skullIngredient3, Ingredient.fromItems(birthingSpirit)).register();

    }

    public static ItemStack setType(ItemStack stack, IAuraType type) {
        if (!stack.hasTag()) stack.setTag(new CompoundNBT());
        assert stack.getTag() != null;
        stack.getTag().putString("stored_type", type.getName().toString());
        return stack;
    }
}
