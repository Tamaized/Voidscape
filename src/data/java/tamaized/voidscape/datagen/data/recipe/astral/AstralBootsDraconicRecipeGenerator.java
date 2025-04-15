package tamaized.voidscape.datagen.data.recipe.astral;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.voidscape.datagen.data.recipe.ExtendedSmithingTransformRecipeBuilder;
import tamaized.voidscape.datagen.data.recipe.IRecipeGenerator;
import tamaized.voidscape.datagen.util.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.tool.ModItemComponentDirectory;

@Component
public class AstralBootsDraconicRecipeGenerator implements IRecipeGenerator {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModItemComponents itemComponents;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void generate(RecipeOutput recipeOutput) {
		ItemStack result = new ItemStack(items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_BOOTS.get());
		result.set(itemComponents.DRACONIC, true);
		ExtendedSmithingTransformRecipeBuilder.extendedSmithing(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_BOOTS.get()),
			Ingredient.of(Items.DRAGON_EGG),
			RecipeCategory.COMBAT,
			result
		)
			.unlocks("has_template", recipeProviderUtil.has(items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "astral_boots_draconic");
	}

}
