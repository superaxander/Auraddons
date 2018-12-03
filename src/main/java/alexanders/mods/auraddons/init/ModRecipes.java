package alexanders.mods.auraddons.init;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.api.recipes.AltarRecipe;
import de.ellpeck.naturesaura.api.recipes.OfferingRecipe;
import de.ellpeck.naturesaura.api.recipes.TreeRitualRecipe;
import de.ellpeck.naturesaura.api.recipes.ing.AmountIngredient;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class ModRecipes {
    @GameRegistry.ObjectHolder("naturesaura:ancient_log")
    public static Item ancientLog;

    @GameRegistry.ObjectHolder("naturesaura:ancient_bark")
    public static Item ancientBark;

    @GameRegistry.ObjectHolder("naturesaura:ancient_planks")
    public static Item ancientPlanks;

    @GameRegistry.ObjectHolder("naturesaura:shockwave_creator")
    public static Item shockwaveCreator;

    @GameRegistry.ObjectHolder("naturesaura:aura_bottle")
    public static Item auraBottle;

    @GameRegistry.ObjectHolder("naturesaura:infused_iron")
    public static Item infusedIron;

    @GameRegistry.ObjectHolder("naturesaura:infused_stone")
    public static Item infusedStone;

    @GameRegistry.ObjectHolder("naturesaura:calling_spirit")
    public static Item callingSpirit;

    @GameRegistry.ObjectHolder("naturesaura:sky_ingot")
    public static Item skyIngot;

    @GameRegistry.ObjectHolder("naturesaura:gold_leaf")
    public static Item goldLeaf;

    public static void init() {
        if (ModConfig.blocks.enableAutoWrath) new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_AUTO_WRATH),
                                                                   Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.ACACIA.getMetadata())),
                                                                   new ItemStack(ModBlocks.autoWrath), ModConfig.general.autoWrathRitualTime, Ingredient.fromItem(infusedStone),
                                                                   IngredientNBT.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_NETHER)),
                                                                   Ingredient.fromItem(infusedStone), Ingredient.fromItem(infusedIron), Ingredient.fromItem(shockwaveCreator),
                                                                   Ingredient.fromItem(infusedIron),
                                                                   IngredientNBT.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_NETHER)),
                                                                   Ingredient.fromItem(infusedStone)).register();
        if (ModConfig.items.enableSkyFeather)
            new OfferingRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SKY_FEATHER), new AmountIngredient(new ItemStack(Items.FEATHER, 1)), Ingredient.fromItem(callingSpirit),
                               new ItemStack(ModItems.skyFeather)).register();
        if (ModConfig.items.enableDampeningFeather && ModConfig.items.enableSkyFeather) GameRegistry
                .addShapedRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_DAMPING_FEATHER), null, new ItemStack(ModItems.dampeningFeather), "F F", "IBI", 'I', skyIngot, 'F',
                                 ModItems.skyFeather, 'B', IngredientNBT.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_OVERWORLD)));
        if (ModConfig.items.enableSkyBottle)
            new OfferingRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SKY_BOTTLE), new AmountIngredient(setType(new ItemStack(auraBottle, 3), NaturesAuraAPI.TYPE_OVERWORLD)),
                               Ingredient.fromItem(callingSpirit), new ItemStack(ModItems.skyBottle)).register();

        if (ModConfig.blocks.enableDisruptionCatalyst) {
            new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_DISRUPTION_CATALYST),
                                 Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata())), new ItemStack(ModBlocks.disruptionCatalyst), 600,
                                 Ingredient.fromStacks(new ItemStack(Blocks.TNT)),
                                 Ingredient.fromStacks(new ItemStack(infusedStone)),
                                 Ingredient.fromItem(Items.GUNPOWDER),
                                 Ingredient.fromItem(skyIngot),
                                 Ingredient.fromItem(goldLeaf),
                                 Ingredient.fromStacks(new ItemStack(Blocks.TNT))).register();
            
            Ingredient destructionCatalyst = Ingredient.fromStacks(new ItemStack(ModBlocks.disruptionCatalyst));
            new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_QUARTZ_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.QUARTZ_BLOCK)),
                            new ItemStack(Items.QUARTZ, 4), destructionCatalyst, 150, 100).register();
            new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_GRAVEL_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)), new ItemStack(Items.FLINT, 2),
                            destructionCatalyst, 100, 50).register();
            //new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_STONE_BRICKS_DISRUPTION), Ingredient
            //        .fromStacks(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.DEFAULT_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.MOSSY_META),
            //                    new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CRACKED_META), new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.CHISELED_META)),
            //                new ItemStack(Blocks.STONE, 4), destructionCatalyst, 25, 20).register();
            for (BlockPlanks.EnumType e : BlockPlanks.EnumType.values()) {
                new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION + "_" + e.getName()), Ingredient
                        .fromStacks(new ItemStack(e.getMetadata() > 3 ? Blocks.LOG2 : Blocks.LOG, 1, e.getMetadata() > 3 ? e.getMetadata() - 4 : e.getMetadata())),
                                new ItemStack(Blocks.PLANKS, 4, e.getMetadata()), destructionCatalyst, 150, 150).register();
            }
            new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_WOOD_DISRUPTION_ANCIENT), Ingredient.fromItems(ancientLog, ancientBark), new ItemStack(ancientPlanks, 4),
                            destructionCatalyst, 150, 150).register();

            for (EnumDyeColor color : EnumDyeColor.values()) {
                new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_CONCRETE_DISRUPTION+"_"+color.getName()), Ingredient.fromStacks(new ItemStack(Blocks.CONCRETE, 1, color.getMetadata())), new ItemStack(Blocks.CONCRETE_POWDER, 1, color.getMetadata()), destructionCatalyst, 100, 50).register();
            }
            
            new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_SANDSTONE_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.SANDSTONE)), new ItemStack(Blocks.SAND, 4), destructionCatalyst, 100, 50).register();
            new AltarRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_RED_SANDSTONE_DISRUPTION), Ingredient.fromStacks(new ItemStack(Blocks.RED_SANDSTONE)), new ItemStack(Blocks.SAND, 4, 1), destructionCatalyst, 100, 50).register();
        }
    }

    private static ItemStack setType(ItemStack stack, IAuraType type) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        assert stack.getTagCompound() != null;
        stack.getTagCompound().setString("stored_type", type.getName().toString());
        return stack;
    }
}
