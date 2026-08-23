package tamaized.voidscape.datagen.data.recipe.ichor;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class IchorCrossbowFangRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModItemComponents itemComponents;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ItemStackTemplate result = new ItemStackTemplate(
			items.toolSetComponentDirectory().ichorToolSet().ICHOR_XBOW.get(),
			DataComponentPatch.builder().set(itemComponents.FANG.get(), true).build()
		);
		new SmithingTransformRecipeBuilder(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(items.toolSetComponentDirectory().ichorToolSet().ICHOR_XBOW.get()),
			Ingredient.of(items.augmentItems().ETHEREAL_SPIDER_FANG.get()),
			RecipeCategory.COMBAT,
			result
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "ichor_crossbow_fang");
	}

}
