package tamaized.voidscape.datagen.data.recipe.voidic;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class VoidicSwordRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		SmithingTransformRecipeBuilder.smithing(
			Ingredient.of(items.augmentItems().VOIDIC_TEMPLATE.get()),
			Ingredient.of(Items.NETHERITE_SWORD),
			Ingredient.of(items.materialItems().VOIDIC_CRYSTAL.get()),
			RecipeCategory.COMBAT,
			items.toolSetComponentDirectory().voidicCrystalToolSet().VOIDIC_CRYSTAL_SWORD.get()
		)
			.unlocks("has_template", recipeProviderUtil.has(itemProvider, items.augmentItems().VOIDIC_TEMPLATE.get()))
			.save(recipeOutput, "voidic_crystal_sword");
	}

}
