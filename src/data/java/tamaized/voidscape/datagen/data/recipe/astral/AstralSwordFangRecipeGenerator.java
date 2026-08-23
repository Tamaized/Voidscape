package tamaized.voidscape.datagen.data.recipe.astral;

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
public class AstralSwordFangRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModItemComponents itemComponents;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ItemStackTemplate result = new ItemStackTemplate(
			items.toolSetComponentDirectory().astralToolSet().ASTRAL_SWORD.get(),
			DataComponentPatch.builder().set(itemComponents.FANG.get(), true).build()
		);
		new SmithingTransformRecipeBuilder(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(items.toolSetComponentDirectory().astralToolSet().ASTRAL_SWORD.get()),
			Ingredient.of(items.augmentItems().ETHEREAL_SPIDER_FANG.get()),
			RecipeCategory.COMBAT,
			result
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "astral_sword_fang");
	}

}
