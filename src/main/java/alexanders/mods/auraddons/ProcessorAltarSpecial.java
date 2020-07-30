package alexanders.mods.auraddons;

import net.minecraft.block.WoodType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

import java.lang.reflect.Field;

public class ProcessorAltarSpecial implements IComponentProcessor {
    private static Field INPUT_FIELD_CACHE;
    private static Field OUTPUT_FIELD_CACHE;
    private static Field CATALYST_FIELD_CACHE;
    private Ingredient input = Ingredient.EMPTY;
    private Ingredient output = Ingredient.EMPTY;
    private Ingredient catalyst = Ingredient.EMPTY;
    private String name = null;

    // Copied from nature's aura
    public static Object getRecipe(String type, String name) {
        if (Minecraft.getInstance().world != null) {
            RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
            ResourceLocation res = new ResourceLocation(name);
            ResourceLocation pre = new ResourceLocation(res.getNamespace(), type + "/" + res.getPath());
            return manager.getRecipe(pre).orElse(null);
        }
        return null;
    }

    private static Field getField(Object recipe, String fieldName) {
        try {
            Field field = recipe.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            Auraddons.logger.error(
                    "Please check if there is an Auraddons update if Auraddons' altar recipes are showing up incorrectly in the book",
                    e);
            return null;
        }
    }

    private static Ingredient getInput(Object recipe) {
        if (INPUT_FIELD_CACHE == null) {
            INPUT_FIELD_CACHE = getField(recipe, "input");
        }
        try {
            //noinspection ConstantConditions
            return (Ingredient) INPUT_FIELD_CACHE.get(recipe);
        } catch (IllegalAccessException | ClassCastException e) {
            Auraddons.logger.error(
                    "Please check if there is an Auraddons update if Auraddons' altar recipes are showing up incorrectly in the book",
                    e);
        } catch (NullPointerException ignored) {
            // no logging because we already logged when returning null
        }
        return Ingredient.EMPTY;
    }

    private static ItemStack getOutput(Object recipe) {
        if (OUTPUT_FIELD_CACHE == null) {
            OUTPUT_FIELD_CACHE = getField(recipe, "output");
        }
        try {
            //noinspection ConstantConditions
            return (ItemStack) OUTPUT_FIELD_CACHE.get(recipe);
        } catch (IllegalAccessException | ClassCastException e) {
            Auraddons.logger.error(
                    "Please check if there is an Auraddons update if Auraddons' altar recipes are showing up incorrectly in the book",
                    e);
        } catch (NullPointerException ignored) {
            // no logging because we already logged when returning null
        }
        return ItemStack.EMPTY;
    }

    private static Ingredient getCatalyst(Object recipe) {
        if (CATALYST_FIELD_CACHE == null) {
            CATALYST_FIELD_CACHE = getField(recipe, "catalyst");
        }
        try {
            //noinspection ConstantConditions
            return (Ingredient) CATALYST_FIELD_CACHE.get(recipe);
        } catch (IllegalAccessException | ClassCastException e) {
            Auraddons.logger.error(
                    "Please check if there is an Auraddons update if Auraddons' altar recipes are showing up incorrectly in the book",
                    e);
        } catch (NullPointerException ignored) {
            // no logging because we already logged when returning null
        }
        return Ingredient.EMPTY;
    }

    @Override
    public void setup(IVariableProvider<String> provider) {
        String recipeName = provider.get("recipe");
        String suffix = provider.get("suffix");
        switch (suffix) {
            case "color":
                for (DyeColor color : DyeColor.values()) {
                    addRecipe(getRecipe("altar", recipeName + "_" + color.getName()));
                }
                break;
            case "wood":
                WoodType.getValues().forEach(type -> {
                    addRecipe(getRecipe("altar", recipeName + "_" + type.getName()));
                });
                addRecipe(getRecipe("altar", recipeName + "_ancient_log"));
                addRecipe(getRecipe("altar", recipeName + "_ancient_bark"));
                break;
            default:
                if (suffix.contains(";")) {
                    String[] suffices = suffix.split(";");
                    for (String suf : suffices) {
                        if (suf.isEmpty()) addRecipe(getRecipe("altar", recipeName));
                        else addRecipe(getRecipe("altar", recipeName + "_" + suf));
                    }
                } else {
                    Auraddons.logger.warn("Unknown suffix type: {}", suffix);
                }
                break;
        }
        name = recipeName;
    }

    @Override
    public String process(String key) {
        switch (key) {
            case "input":
                return PatchouliAPI.instance.serializeIngredient(input);
            case "output":
                return PatchouliAPI.instance.serializeIngredient(output);
            case "catalyst":
                if (catalyst != Ingredient.EMPTY) return PatchouliAPI.instance.serializeIngredient(catalyst);
                else return null;
            case "name":
                return I18n.format(name);
            default:
                return null;
        }
    }

    private void addRecipe(Object recipe) {
        if (recipe != null) {
            input = Ingredient.fromStacks(
                    ArrayUtils.addAll(input.getMatchingStacks(), getInput(recipe).getMatchingStacks()));
            output = Ingredient.fromStacks(
                    ArrayUtils.addAll(output.getMatchingStacks(), getOutput(recipe)));
            catalyst = Ingredient.fromStacks(
                    ArrayUtils.addAll(catalyst.getMatchingStacks(), getCatalyst(recipe).getMatchingStacks()));
        }
    }
}
