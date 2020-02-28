package alexanders.mods.auraddons;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.recipes.AltarRecipe;
import net.minecraft.block.WoodType;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.DyeColor;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorAltarSpecial implements IComponentProcessor {
    private Ingredient input = Ingredient.EMPTY;
    private Ingredient output = Ingredient.EMPTY;
    private Ingredient catalyst = Ingredient.EMPTY;
    private String name = null;

    @Override
    public void setup(IVariableProvider<String> provider) {
        String recipeName = provider.get("recipe");
        String suffix = provider.get("suffix");
        switch (suffix) {
            case "color":
                for (DyeColor color : DyeColor.values()) {
                    addRecipe(NaturesAuraAPI.ALTAR_RECIPES.get(new ResourceLocation(recipeName + "_" + color.getName())));
                }
                break;
            case "wood":
                WoodType.getValues().forEach(type -> {
                    addRecipe(NaturesAuraAPI.ALTAR_RECIPES.get(new ResourceLocation(recipeName + "_" + type.getName())));
                });
                addRecipe(NaturesAuraAPI.ALTAR_RECIPES.get(new ResourceLocation(recipeName + "_ancient_log")));
                addRecipe(NaturesAuraAPI.ALTAR_RECIPES.get(new ResourceLocation(recipeName + "_ancient_bark")));
                break;
            default:
                if (suffix.contains(";")) {
                    String[] suffices = suffix.split(";");
                    for (String suf : suffices) {
                        if (suf.isEmpty()) addRecipe(NaturesAuraAPI.ALTAR_RECIPES.get(new ResourceLocation(recipeName)));
                        else addRecipe(NaturesAuraAPI.ALTAR_RECIPES.get(new ResourceLocation(recipeName + "_" + suf)));
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

    private void addRecipe(AltarRecipe recipe) {
        if (recipe != null) {
            input = Ingredient.fromStacks(ArrayUtils.addAll(input.getMatchingStacks(), recipe.input.getMatchingStacks()));
            output = Ingredient.fromStacks(ArrayUtils.addAll(output.getMatchingStacks(), recipe.output));
            catalyst = Ingredient.fromStacks(ArrayUtils.addAll(catalyst.getMatchingStacks(), recipe.catalyst.getMatchingStacks()));
        }
    }
}
