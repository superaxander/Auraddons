package alexanders.mods.auraddons;

import de.ellpeck.naturesaura.api.aura.type.IAuraType;
import de.ellpeck.naturesaura.recipes.AltarRecipe;
import net.minecraft.block.WoodType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.ArrayUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ProcessorAltarSpecial implements IComponentProcessor {
    @ObjectHolder("naturesaura:aura_bottle")
    public static Item auraBottle;

    private Ingredient input = Ingredient.EMPTY;
    private Ingredient output = Ingredient.EMPTY;
    private Ingredient catalyst = Ingredient.EMPTY;
    private String name = null;
    private Ingredient requiredType = Ingredient.EMPTY;

    // Copied from nature's aura
    public static AltarRecipe getRecipe(String name) {
        if (Minecraft.getInstance().world != null) {
            RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
            ResourceLocation res = new ResourceLocation(name);
            ResourceLocation pre = new ResourceLocation(res.getNamespace(), "altar/" + res.getPath());
            try {
                return (AltarRecipe) manager.getRecipe(pre).orElse(null);
            } catch (ClassCastException e) {
                Auraddons.logger.debug("AltarRecipeProcessor error", e);
                return null;
            }
        }
        return null;
    }

    private static ItemStack setType(ItemStack stack, IAuraType type) {
        stack.getOrCreateTag().putString("stored_type", type.getName().toString());
        return stack;
    }

    @Override
    public void setup(IVariableProvider provider) {
        String recipeName = provider.get("recipe").asString();
        String suffix = provider.get("suffix").asString();
        switch (suffix) {
            case "color":
                for (DyeColor color : DyeColor.values()) {
                    addRecipe(getRecipe(recipeName + "_" + color.getTranslationKey()));
                }
                break;
            case "wood":
                WoodType.getValues().forEach(type -> addRecipe(getRecipe(recipeName + "_" + type.getName())));
                addRecipe(getRecipe(recipeName + "_ancient_log"));
                addRecipe(getRecipe(recipeName + "_ancient_bark"));
                break;
            default:
                if (suffix.contains(";")) {
                    String[] suffices = suffix.split(";");
                    for (String suf : suffices) {
                        if (suf.isEmpty()) addRecipe(getRecipe(recipeName));
                        else addRecipe(getRecipe(recipeName + "_" + suf));
                    }
                } else {
                    Auraddons.logger.warn("Unknown suffix type: {}", suffix);
                }
                break;
        }
        name = recipeName;
    }

    private void addRecipe(AltarRecipe recipe) {
        if (recipe != null) {
            input = Ingredient.fromStacks(
                    ArrayUtils.addAll(input.getMatchingStacks(), recipe.input.getMatchingStacks()));
            output = Ingredient.fromStacks(
                    ArrayUtils.addAll(output.getMatchingStacks(), recipe.output));
            catalyst = Ingredient.fromStacks(
                    ArrayUtils.addAll(catalyst.getMatchingStacks(), recipe.catalyst.getMatchingStacks()));
            if (recipe.requiredType == null) {
                requiredType = Ingredient.fromStacks(
                        ArrayUtils.addAll(requiredType.getMatchingStacks(), ItemStack.EMPTY));
            } else {
                requiredType = Ingredient.fromStacks(ArrayUtils.addAll(requiredType.getMatchingStacks(),
                        setType(new ItemStack(auraBottle), recipe.requiredType)));
            }
        }
    }

    @Override
    @Nonnull
    public IVariable process(String key) {
        switch (key) {
            case "input":
                return IVariable.wrapList(
                        Arrays.stream(input.getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
            case "output":
                return IVariable.wrapList(
                        Arrays.stream(output.getMatchingStacks()).map(IVariable::from).collect(Collectors.toList()));
            case "type":
                return IVariable.wrapList(Arrays.stream(requiredType.getMatchingStacks()).map(IVariable::from).collect(
                        Collectors.toList()));
            case "catalyst":
                if (catalyst != Ingredient.EMPTY)
                    return IVariable.wrapList(Arrays.stream(catalyst.getMatchingStacks()).map(IVariable::from).collect(
                            Collectors.toList()));
                else return null;
            case "name":
                return IVariable.wrap(I18n.format(name));
            default:
                return null;
        }
    }

    public boolean allowRender(String group) {
        return group.isEmpty() || group.equals(catalyst.equals(Ingredient.EMPTY) ? "altar" : "catalyst");
    }
}
