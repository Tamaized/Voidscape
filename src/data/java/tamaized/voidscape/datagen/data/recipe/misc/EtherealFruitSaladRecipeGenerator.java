package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModItemComponentDirectory;

@Component
public class EtherealFruitSaladRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModItemComponentDirectory items;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ShapelessRecipeBuilder.shapeless(
				itemProvider,
				RecipeCategory.FOOD,
				items.etherealFruitItems().ETHEREAL_FRUIT_SALAD.get()
			)
			.requires(Items.BOWL)
			.requires(items.etherealFruitItems().ETHEREAL_FRUIT_VOID.get())
			.requires(items.etherealFruitItems().ETHEREAL_FRUIT_NULL.get())
			.requires(items.etherealFruitItems().ETHEREAL_FRUIT_OVERWORLD.get())
			.requires(items.etherealFruitItems().ETHEREAL_FRUIT_NETHER.get())
			.requires(items.etherealFruitItems().ETHEREAL_FRUIT_END.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, Items.BOWL))
			.save(recipeOutput, "ethereal_fruit_salad");
	}

}
