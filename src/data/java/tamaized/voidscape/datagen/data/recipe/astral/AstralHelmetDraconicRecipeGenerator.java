package tamaized.voidscape.datagen.data.recipe.astral;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponents;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class AstralHelmetDraconicRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private ModItemComponents itemComponents;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ItemStackTemplate result = new ItemStackTemplate(
			items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_HELMET.get(),
			DataComponentPatch.builder().set(itemComponents.DRACONIC.get(), true).build()
		);
		new SmithingTransformRecipeBuilder(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(items.modArmorSetComponentDirectory().astralArmorSet().ASTRAL_HELMET.get()),
			Ingredient.of(Items.DRAGON_EGG),
			RecipeCategory.COMBAT,
			result
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "astral_helmet_draconic");
	}

}
