package tamaized.voidscape.datagen.data.recipe.corrupt;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class CorruptLeggingsRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		SmithingTransformRecipeBuilder.smithing(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(items.modArmorSetComponentDirectory().voidicCrystalArmorSet().VOIDIC_CRYSTAL_LEGS.get()),
			Ingredient.of(items.materialItems().TENDRIL.get()),
			RecipeCategory.COMBAT,
			items.modArmorSetComponentDirectory().corruptArmorSet().CORRUPT_LEGS.get()
		)
			.unlocks("has_template", recipeProviderUtil.has(items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "corrupt_leggings");
	}

}
