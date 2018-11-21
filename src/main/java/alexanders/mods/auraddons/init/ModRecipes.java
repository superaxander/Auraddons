package alexanders.mods.auraddons.init;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.api.recipes.TreeRitualRecipe;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
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


    public static void init() {
        new TreeRitualRecipe(new ResourceLocation(MOD_ID, ModNames.RECIPE_AUTO_WRATH),
                             Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.ACACIA.getMetadata())), new ItemStack(ModBlocks.autoWrath),
                             ModConfig.general.autoWrathRitualTime, Ingredient.fromItem(infusedStone),
                             IngredientNBT.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_NETHER)), Ingredient.fromItem(infusedStone),
                             Ingredient.fromItem(infusedIron), Ingredient.fromItem(shockwaveCreator), Ingredient.fromItem(infusedIron),
                             IngredientNBT.fromStacks(setType(new ItemStack(auraBottle), NaturesAuraAPI.TYPE_NETHER)), Ingredient.fromItem(infusedStone)).register();
    }

    private static ItemStack setType(ItemStack stack, IAuraType type) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        assert stack.getTagCompound() != null;
        stack.getTagCompound().setString("stored_type", type.getName().toString());
        return stack;
    }
}
