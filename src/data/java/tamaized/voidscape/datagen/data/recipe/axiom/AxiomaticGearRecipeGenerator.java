package tamaized.voidscape.datagen.data.recipe.axiom;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class AxiomaticGearRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		SmithingTransformRecipeBuilder.smithing(
			Ingredient.of(items.materialItems().AXIOMATIC_GEAR_LEFT.get()),
			Ingredient.of(items.materialItems().ASTRAL_ESSENCE.get()),
				Ingredient.of(items.materialItems().AXIOMATIC_GEAR_RIGHT.get()),
			RecipeCategory.MISC,
			items.materialItems().AXIOMATIC_GEAR.get()
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, items.materialItems().ASTRAL_ESSENCE.get()))
			.save(recipeOutput, "axiomatic_gear");
	}

}
