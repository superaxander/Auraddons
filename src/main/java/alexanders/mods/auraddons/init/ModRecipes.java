package alexanders.mods.auraddons.init;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.api.recipes.OfferingRecipe;
import de.ellpeck.naturesaura.api.recipes.TreeRitualRecipe;
import de.ellpeck.naturesaura.api.recipes.ing.AmountIngredient;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static alexanders.mods.auraddons.Constants.MOD_ID;

public class ModRecipes {
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
    }

    private static ItemStack setType(ItemStack stack, IAuraType type) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        assert stack.getTagCompound() != null;
        stack.getTagCompound().setString("stored_type", type.getName().toString());
        return stack;
    }
}
