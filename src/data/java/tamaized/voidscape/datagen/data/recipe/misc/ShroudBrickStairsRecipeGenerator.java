package tamaized.voidscape.datagen.data.recipe.misc;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import tamaized.datagenutil.data.recipe.RecipeHolder;
import tamaized.datagenutil.data.recipe.RecipeProviderUtil;
import tamaized.voidscape.registry.ModBlockComponentDirectory;

@Component
public class ShroudBrickStairsRecipeGenerator extends RecipeHolder {

	@Autowired
	private ModBlockComponentDirectory blocks;

	@Autowired
	private RecipeProviderUtil recipeProviderUtil;

	@Override
	public void make(HolderLookup.Provider provider, HolderGetter<Item> itemProvider, RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(
				itemProvider,
				RecipeCategory.BUILDING_BLOCKS,
				blocks.thunderForestBiomeBlocks().THUNDER_STAIRS.get(),
				4
			)
			.pattern("P  ")
			.pattern("PP ")
			.pattern("PPP")
			.define('P', blocks.materialBlocks().SHROUD_BRICK.get())
			.unlockedBy("has_template", recipeProviderUtil.has(itemProvider, blocks.materialBlocks().SHROUD_BRICK_STAIRS.get()))
			.save(recipeOutput, "shroud_brick_stairs");
	}

}
