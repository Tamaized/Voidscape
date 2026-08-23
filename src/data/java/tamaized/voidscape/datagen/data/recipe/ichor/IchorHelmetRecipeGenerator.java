package tamaized.voidscape.datagen.data.recipe.ichor;

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
public class IchorHelmetRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		SmithingTransformRecipeBuilder.smithing(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(items.modArmorSetComponentDirectory().titaniteArmorSet().TITANITE_HELMET.get()),
			Ingredient.of(items.materialItems().ICHOR_CRYSTAL.get()),
			RecipeCategory.COMBAT,
			items.modArmorSetComponentDirectory().ichorArmorSet().ICHOR_HELMET.get()
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "ichor_helmet");
	}

}
